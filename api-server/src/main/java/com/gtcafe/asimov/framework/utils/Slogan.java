package com.gtcafe.asimov.framework.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.gtcafe.asimov.framework.bean.singleton.Releng;

@Configuration
public class Slogan {

    private static final Logger logger = LoggerFactory.getLogger(Slogan.class.getName());

    @Value("classpath:slogan.txt")
    Resource _slogan;

    @Value("classpath:web-runtime.txt")
    Resource _webRuntime;

    @Value("classpath:user-claim.txt")
    Resource _userClaim;

    @Value("classpath:release-notes.txt")
    Resource _releaseNotes;

    @Autowired
    private Releng releng;

    private String getFileContent(Resource resource) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));

            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }

    public String slogan() {
        String content = getFileContent(_slogan);

        content = content.replaceAll("<ServiceName>", releng.getServiceName());
        content = content.replaceAll("<Version>", releng.getVersion());
        content = content.replaceAll("<BuildType>", releng.getBuildType());
        content = content.replaceAll("<BuildId>", releng.getBuildId());
        content = content.replaceAll("<HashCode>", releng.getHashcode());

        content = content.replaceAll("<BuildOs>", releng.getBuildOs());
        content = content.replaceAll("<BuildSdk>", releng.getBuildSdk());

        String runtimeSdk = String.format("%s %s", System.getProperty("java.version"), System.getProperty("java.vendor"));
        String runtimeOs = String.format("%s %s %s", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
        content = content.replaceAll("<RuntimeSdk>",  runtimeSdk);
        content = content.replaceAll("<RuntimeOS>",  runtimeOs);

        return content;
    }

    public String userClaim() {
        String content = getFileContent(_userClaim);

        content = content.replaceAll("<TenantName>", "lds");
        content = content.replaceAll("<UserName>", "rickhwang");
        content = content.replaceAll("<RoleName>", "admin");
        // slogan = slogan.replaceAll("<Request-Id>", reqId);

        return content;
    }

    public String webruntime(String reqId) {
        String content = getFileContent(_webRuntime);

        content = content.replaceAll("<Request-Id>", reqId);
        content = content.replaceAll("<Datetime>", ZonedDateTime.now().toString());

        return content;
    }

    public String apiSlogan(String reqId) {
        StringBuffer buf = new StringBuffer();
        buf.append(slogan());
        buf.append(userClaim());
        buf.append(webruntime(reqId));
        return buf.toString();
    }

    public String releaseNotes() {
        String content = getFileContent(_releaseNotes);

        return content;
    }

}