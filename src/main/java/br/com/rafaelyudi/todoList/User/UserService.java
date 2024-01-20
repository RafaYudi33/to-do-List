package br.com.rafaelyudi.todoList.User;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rafaelyudi.todoList.Errors.UserAlreadyExistsException;
import br.com.rafaelyudi.todoList.Mapper.ModelMapperConfig;

@Service
public class UserService {

     @Autowired
     private IUserRepository userRepository;


     public String passCript(UserDTO data) {
          // var user = this.userRepository.findByUsername(username);
          var passwordCript = BCrypt.withDefaults().hashToString(12, data.getPassword().toCharArray());
          return passwordCript;
     }

     public UserDTO userCreate(UserDTO data) {

          var verifyUserAlreadyExists = this.userRepository.findByUsername(data.getUsername());

          if (verifyUserAlreadyExists != null) {
               throw new UserAlreadyExistsException("Esse nome de usuário ja existe!");
          }

          var passCript = passCript(data);
          var userModel = ModelMapperConfig.parseObject(data, UserModel.class);
          userModel.setPassword(passCript);
          userSave(userModel);

          return ModelMapperConfig.parseObject(userModel, UserDTO.class);
     }

     public void userSave(@NonNull UserModel user) {
          this.userRepository.save(user);
     }

}
