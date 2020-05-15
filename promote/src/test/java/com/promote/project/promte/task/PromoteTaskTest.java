package com.promote.project.promte.task;

import com.promote.project.promote.task.PromoteTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 6550
 * @date 2020/4/21 下午 04:37
 * @description
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class PromoteTaskTest {

    @Autowired
    PromoteTask promoteTask;

    @Test
    public void testFtp() throws Exception {
//        promoteTask.ftpFetchFile();
//        promoteTask.dealDiffData("d:/hostel.xlsx",true);
        promoteTask.dealWhitelistFile("d:/Business.csv",false);
    }
}
