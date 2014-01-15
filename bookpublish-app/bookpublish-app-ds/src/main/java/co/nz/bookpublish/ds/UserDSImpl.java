package co.nz.bookpublish.ds;

import static co.nz.bookpublish.data.predicates.UserPredicates.findByUsername;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.nz.bookpublish.DuplicatedException;
import co.nz.bookpublish.NotFoundException;
import co.nz.bookpublish.converter.UserConverter;
import co.nz.bookpublish.data.UserDto;
import co.nz.bookpublish.data.UserModel;
import co.nz.bookpublish.data.repository.UserRepository;
import co.nz.bookpublish.utils.GeneralUtils;

@Service
@Transactional(value = "localTxManager", readOnly = true)
public class UserDSImpl implements UserDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDSImpl.class);

	@Resource
	private UserConverter userConverter;

	@Resource
	private UserRepository userRepository;

	@Resource
	private IdentityService identityService;

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public UserDto createUser(String username, String password)
			throws Exception {
		LOGGER.info("createUser start:{}");
		LOGGER.info("username{}", username);
		LOGGER.info("password:{}", password);
		UserDto added = null;
		UserModel userModel = userRepository.findOne(findByUsername(username));
		if (userModel != null
				|| identityService.createUserQuery().userId(username).count() > 0) {
			throw new DuplicatedException("User already existed with name["
					+ username + "]");
		}

		userModel = UserModel.getBuilder(username,
				GeneralUtils.pwdEncode(password)).build();
		userModel.setCreateTime(new Date());
		userRepository.save(userModel);

		User wfUser = identityService.newUser(username);
		identityService.saveUser(wfUser);

		added = userConverter.toDto(userModel);
		LOGGER.info("createUser end:{}", added);
		return added;
	}

	@Override
	public UserDto getUserById(Long userId) throws Exception {
		LOGGER.info("getUserById start:{}", userId);
		UserDto found = null;
		UserModel userModel = userRepository.findOne(userId);
		if (userModel == null) {
			throw new NotFoundException("User not found by id[" + userId + "]");
		}
		found = userConverter.toDto(userModel);
		LOGGER.info("getUserById end:{}", found);
		return found;
	}

	@Override
	public UserDto getUserByName(String username) throws Exception {
		LOGGER.info("getUserByName start:{}", username);
		UserDto found = null;
		UserModel userModel = userRepository.findOne(findByUsername(username));
		if (userModel == null) {
			throw new NotFoundException("User not found by username["
					+ username + "]");
		}
		found = userConverter.toDto(userModel);
		LOGGER.info("getUserByName end:{}", found);
		return found;
	}

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public void updateUser(Long userId, UserDto userDto) throws Exception {
		UserModel userModel = userRepository.findOne(userId);
		if (userModel == null) {
			throw new NotFoundException("User not found by id[" + userId + "]");
		}
		if (!StringUtils.isEmpty(userDto.getUsername())) {
			// username is same as the userId of wfuser,
			String originUsername = userModel.getUsername();
			String newUsername = userDto.getUsername();
			userModel.setUsername(newUsername);

			if (identityService.createUserQuery().userId(originUsername)
					.count() > 0) {
				List<Group> groupList = identityService.createGroupQuery()
						.groupMember(originUsername).list();
				if (groupList != null) {
					for (Group group : groupList) {
						identityService.deleteMembership(originUsername,
								group.getId());
						identityService.createMembership(newUsername,
								group.getId());
					}
				}

				identityService.deleteUser(originUsername);
				User wfUser = identityService.newUser(newUsername);
				identityService.saveUser(wfUser);
			}

		}
		if (!StringUtils.isEmpty(userDto.getPassword())) {
			userModel.setPassword(userDto.getPassword());
		}
	}

}
