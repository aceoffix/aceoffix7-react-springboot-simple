package com.acesoft.aceoffix7springbootsimple.util;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.File;
import java.io.IOException;

public class GetDirPathUtil {
    public static String getDirPath(){
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:");
            if (resources.length > 0) {
                File classpathRoot = new File(resources[0].getURI());
                File targetDir = new File(classpathRoot.getParentFile().getParentFile(), "target/classes");
                if (targetDir.exists()) {
                    return targetDir.getAbsolutePath();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
