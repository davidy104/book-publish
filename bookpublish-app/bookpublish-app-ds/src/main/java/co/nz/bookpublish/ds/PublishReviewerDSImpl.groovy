package co.nz.bookpublish.ds

import static co.nz.bookpublish.data.predicates.PublishReviewerPredicates.findByReviewerIdentity
import static co.nz.bookpublish.data.predicates.PublishReviewerPredicates.findByUserAccount
import static co.nz.bookpublish.data.predicates.UserPredicates.findByUsername
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import org.activiti.engine.IdentityService
import org.activiti.engine.identity.Group
import org.activiti.engine.identity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import co.nz.bookpublish.DuplicatedException
import co.nz.bookpublish.NotFoundException
import co.nz.bookpublish.converter.PublishReviewerConverter
import co.nz.bookpublish.converter.UserConverter
import co.nz.bookpublish.data.PublishAdminReviewerModel
import co.nz.bookpublish.data.PublishAdministratorModel
import co.nz.bookpublish.data.PublishReviewerDto
import co.nz.bookpublish.data.PublishReviewerLoadStrategies
import co.nz.bookpublish.data.PublishReviewerModel
import co.nz.bookpublish.data.UserDto
import co.nz.bookpublish.data.UserModel
import co.nz.bookpublish.data.repository.PublishAdminReviewerRepository
import co.nz.bookpublish.data.repository.PublishAdministratorRepository
import co.nz.bookpublish.data.repository.PublishReviewerRepository
import co.nz.bookpublish.data.repository.UserRepository

@Service
@Transactional(value = "localTxManager", readOnly = true)
@Slf4j
class PublishReviewerDSImpl implements PublishReviewerDS{

	@Resource
	IdentityService identityService

	@Resource
	PublishReviewerRepository publishReviewerRepository

	@Resource
	PublishAdminReviewerRepository publishAdminReviewerRepository

	@Resource
	PublishAdministratorRepository publishAdministratorRepository

	@Resource
	PublishReviewerConverter publishReviewerConverter

	@Resource
	UserRepository userRepository

	@Resource
	UserConverter userConverter

	@Override
	@Transactional(value = "localTxManager",readOnly = false)
	PublishReviewerDto createPublishReviewer(
			PublishReviewerDto publishReviewer, Set<Long> selectedAdminIds){
		log.info "createPublishReviewer start:{} $publishReviewer"
		PublishReviewerDto added
		UserModel usermodel
		User wfUser
		PublishReviewerModel model = publishReviewerConverter.toModel(publishReviewer)
		model.setCreateDate(new Date())
		UserDto userdto = publishReviewer.getUser()
		if(userdto){
			if(userdto.userId){
				//use existed user
				usermodel = userRepository.findOne(userdto.userId)
				if(!usermodel){
					throw new NotFoundException("User not found by id [${userdto.userId}]")
				}
				wfUser = identityService.createUserQuery().userId(usermodel.username).singleResult();
			}else if(userdto.username){
				//create a new user
				usermodel = userRepository.findOne(findByUsername(userdto.username))
				if(usermodel || identityService.createUserQuery().userId(usermodel.username).count() > 0){
					throw new DuplicatedException("User ${userdto.username} already exists")
				}
				usermodel = userConverter.toModel(userdto)
				usermodel = userRepository.save(usermodel)
				wfUser = identityService.newUser(usermodel.username)
			}

			model.setUser(usermodel)

			//update existed wfuser or create new one
			wfUser.setEmail(publishReviewer.email)
			wfUser.setFirstName(publishReviewer.firstName)
			wfUser.setLastName(publishReviewer.lastName)
			identityService.saveUser(wfUser)
		}


		if(selectedAdminIds){
			selectedAdminIds.each {
				PublishAdministratorModel publishAdministrator = publishAdministratorRepository.findOne(it)
				if(publishAdministrator){
					PublishAdminReviewerModel publishAdminReviewerModel = new PublishAdminReviewerModel()
					publishAdminReviewerModel.setPublishAdministrator(publishAdministrator)
					publishAdminReviewerModel.setPublishReviewer(model)
					model.addPublishAdminReviewer(publishAdminReviewerModel)

					if (identityService.createGroupQuery().groupId(publishAdministrator.getName()).count() <= 0) {
						Group group = identityService.newGroup(publishAdministrator.getName());
						group.setName(publishAdministrator.getName());
						group.setType("");
						identityService.saveGroup(group);
					}

					if(usermodel){
						identityService.createMembership(usermodel.username, publishAdministrator.getName())
					}
				}
			}
		}

		model = publishReviewerRepository.save(model)
		added = publishReviewerConverter.toDto(model)
		log.info "createPublishReviewer end:{} $added"
		return added
	}

	@Override
	PublishReviewerDto getPublishReviewerById(Long reviewerId,
			PublishReviewerLoadStrategies... loadStrategies) {
		log.info "getPublishReviewerById start:{} $reviewerId"
		PublishReviewerDto found
		PublishReviewerModel foundModel = publishReviewerRepository.findOne(reviewerId)
		if(!foundModel){
			throw new NotFoundException("Reviewer not found by id [$reviewerId]")
		}
		found = publishReviewerConverter.toDto(foundModel, loadStrategies)
		log.info "getPublishReviewerById end:{} $found"
		return found
	}

	@Override
	PublishReviewerDto getPublishReviewerByIdentity(String identity,
			PublishReviewerLoadStrategies... loadStrategies)  {
		log.info "getPublishReviewerByIdentity start:{} $identity"
		PublishReviewerDto found
		PublishReviewerModel foundModel = publishReviewerRepository.findOne(findByReviewerIdentity(identity))
		if(!foundModel){
			throw new NotFoundException("Reviewer not found by identity [$identity]")
		}
		found = publishReviewerConverter.toDto(foundModel, loadStrategies)
		log.info "getPublishReviewerByIdentity end:{} $found"
		return found
	}


	@Override
	@Transactional(value = "localTxManager",readOnly = false)
	void deletePublishReviewer(Long reviewerId) {
		log.info "deletePublishReviewer start:{} $reviewerId"
		String username
		PublishReviewerModel foundModel = publishReviewerRepository.findOne(reviewerId)

		if(!foundModel){
			throw new NotFoundException("Reviewer not found by id [$reviewerId]")
		}
		UserModel userModel = foundModel.getUser()
		if(userModel){
			log.info "reviewer useraccount:{} $userModel"
			username = userModel.username
			if(identityService.createUserQuery().userId(username).count() > 0){
				if(identityService.createGroupQuery().groupMember(username).count()>0){
					throw new Exception("Deleted can not be executed as reviewer still has member relationship with some groups")
				}
				identityService.deleteUser(username)
				userRepository.delete(userModel)
			}
		}

		Set<PublishAdminReviewerModel> adminReviewers = foundModel.getAdminReviewers()
		if(adminReviewers){
			adminReviewers.each { publishAdminReviewerRepository.delete(it) }
		}

		publishReviewerRepository.delete(foundModel)
		log.info "deletePublishReviewer end:{}"
	}



	@Override
	public PublishReviewerDto getPublishReviewerByUser(String username)
	throws Exception {
		log.info "getPublishReviewerByUser start:{} $username"
		PublishReviewerDto found
		PublishReviewerModel foundModel = publishReviewerRepository.findOne(findByUserAccount(username))
		if(!foundModel){
			throw new NotFoundException("Reviewer not found by identity [$username]")
		}
		found = publishReviewerConverter.toDto(foundModel,PublishReviewerLoadStrategies.USER)
		log.info "getPublishReviewerByUser end:{} $found"
		return found
	}

	@Override
	public void updatePublishReviewer(Long reviewerId,
			PublishReviewerDto publishReviewer) throws Exception {
	}

	@Override
	public void updatePublishAdministratorForReviewer(Long reviewerId,
			Set<Long> selectedAdminIds) throws Exception {
	}
}
