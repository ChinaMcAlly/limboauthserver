package cn.moonmc.limboAdd.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigFile {
    private final String fillName;

    public ConfigFile(String fillName) {
        this.fillName = fillName;
    }

    /**
     * 获取配置文件读取流
     * 如果配置文件不存在则会从资源文件中复制出来。
     * */
    public BufferedReader getReader() throws IOException {
        Path filePath = Paths.get("./", fillName);


        if (!Files.exists(filePath)) {
            InputStream stream = getClass().getResourceAsStream( "/" + fillName);

            if (stream == null)
                throw new FileNotFoundException("在资源文件中没有找到"+fillName);

            Files.copy(stream, filePath);
        }

        return Files.newBufferedReader(filePath);
    }

    public File getFile() throws IOException {
        Path filePath = Paths.get("./", fillName);


        if (!Files.exists(filePath)) {
            InputStream stream = getClass().getResourceAsStream( "/" + fillName);

            if (stream == null)
                throw new FileNotFoundException("在资源文件中没有找到"+fillName);

            Files.copy(stream, filePath);
        }

        return filePath.toFile();
    }
}
