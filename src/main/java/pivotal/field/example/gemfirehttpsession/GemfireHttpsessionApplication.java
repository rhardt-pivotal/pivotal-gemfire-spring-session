/*
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package pivotal.field.example.gemfirehttpsession;

import org.apache.geode.security.AuthInitialize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.GemfireOperations;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePool;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.gemfire.GemFireOperationsSessionRepository;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;
import org.springframework.session.data.gemfire.config.annotation.web.http.GemFireHttpSessionConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@ClientCacheApplication(logLevel = "debug")
@EnablePool(name = "mposGemFirePool")

//@EnableSecurity(how do I set the client.authentication-initializer class here that is defined in the application.properties)


//@EnableGemFireHttpSession(poolName = "mposGemFirePool",
//        regionName = "Session",
//        maxInactiveIntervalInSeconds = 180,
//        sessionSerializerBeanName =
//                GemFireHttpSessionConfiguration.SESSION_DATA_SERIALIZER_BEAN_NAME
//)
@EnableGemFireHttpSession(poolName = "mposGemFirePool",regionName = "Session", sessionSerializerBeanName =
        GemFireHttpSessionConfiguration.SESSION_DATA_SERIALIZER_BEAN_NAME)

@SpringBootApplication
@Controller
public class GemfireHttpsessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(GemfireHttpsessionApplication.class, args);
    }

    @RequestMapping("/")
    public ResponseEntity<String> home(HttpSession session) {
        return ResponseEntity.ok(dumpSession(session));
    }

    @RequestMapping("/1")
    public ResponseEntity<String> one(HttpSession session) {
        session.setAttribute("KEY-1", "VALUE-1");
        return ResponseEntity.ok("*** ONE:\n"+dumpSession(session));
    }


    @RequestMapping("/2")
    public ResponseEntity<String> two(HttpSession session) {
        session.setAttribute("KEY-2", "VALUE-2");
        return ResponseEntity.ok("*** TWO:\n"+dumpSession(session));
    }

    @RequestMapping("/3")
    public ResponseEntity<String> three(HttpSession session) {
        session.setAttribute("KEY-1", "VALUE-3");
        return ResponseEntity.ok("*** THREE:\n"+dumpSession(session));
    }

    private String dumpSession(HttpSession session) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> atts = session.getAttributeNames();
        while (atts.hasMoreElements()) {
            String att = atts.nextElement();
            sb.append("\n***** "+att+" : "+session.getAttribute(att));
        }
        return sb.toString();
    }


}
