package com.probodia.userservice.testutil;

import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.user.UserRepository;
import com.probodia.userservice.oauth.entity.ProviderType;
import com.probodia.userservice.oauth.entity.RoleType;
import org.junit.jupiter.api.BeforeEach;

import static com.probodia.userservice.testutil.TestStaticVariable.*;

public class BeforeEachMethod {



    @BeforeEach
    public static void makeUser(UserRepository userRepository){


        User user = new User(
                user_userid,
                user_username,
                null,
                user_emailVerifiedYn,
                user_profileImageUrl,
                ProviderType.KAKAO,
                RoleType.USER);


        userRepository.save(user);
    }
}
