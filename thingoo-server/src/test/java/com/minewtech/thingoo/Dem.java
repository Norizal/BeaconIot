package com.minewtech.thingoo;

import com.minewtech.thingoo.api.Email.RegisterValidateService;
import com.minewtech.thingoo.model.operation.Operation;
import com.minewtech.thingoo.repository.OperationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Dem {
        @Autowired
   private OperationRepository operationRepository;
        @Test
        public  void index(){
            Operation operation=new Operation();
            operation.setGateway_mac("AC233FC0000D");
            operation.setCreatedAt(new Date(System.currentTimeMillis()));
            operation.setUpdatedAt(new Date(System.currentTimeMillis()));
            operation.setOperation("root");

            Operation operation1=operationRepository.save(operation);
            System.out.println(operation1);


        }

        @Test
    public  void indes(){
          RegisterValidateService.processregister("jiayouxxt@gmail.com");

        }



}
