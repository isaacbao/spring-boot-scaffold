package io.github.isaacbao.scaffold;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiMockTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAPI() throws Exception {
        this.mvc.perform(post("/aaa/aaa/aaa")
                .param("aaa", "aaa")
                .param("bbb", "bbb")
        ).andDo(MockMvcResultHandlers.print());
    }


}
