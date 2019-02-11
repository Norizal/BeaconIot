//package com.minewtech.thingoo;
//
//import com.minewtech.thingoo.model.user.Role;
//import com.minewtech.thingoo.model.user.User;
//import com.minewtech.thingoo.repository.UserRepository;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.UUID;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ThingooServerApplicationTests {

//	@Autowired
//	private UserRepository userRepository;

//	@Test
//	public void contextLoads() {
//	}
//
//	@Test
//	public void test() throws Exception {
//		userRepository.save(new User(UUID.randomUUID().toString().replace("-", ""),"demo@beaconyun.com", "demo", "demo", Role.USER, ""));
////		userRepository.save(new User(UUID.randomUUID().toString().replace("-", ""),"admin@beaconyun.com", "admin", "admin", Role.ADMIN, ""));
//
//		User user = userRepository.findOneByEmail("demo@beaconyun.com").orElse(null);
//
//		Assert.assertNotNull(user);
//
//		Assert.assertEquals("demo", user.getName());
//
////		userRepository.delete(user);
//
//
//	}
//}
