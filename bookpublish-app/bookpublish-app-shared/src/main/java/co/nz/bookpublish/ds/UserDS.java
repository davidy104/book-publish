package co.nz.bookpublish.ds;

import co.nz.bookpublish.data.UserDto;

public interface UserDS {

	UserDto createUser(String username, String password) throws Exception;

	UserDto getUserById(Long userId) throws Exception;

	UserDto getUserByName(String username) throws Exception;

	void updateUser(Long userId, UserDto userDto) throws Exception;

}
