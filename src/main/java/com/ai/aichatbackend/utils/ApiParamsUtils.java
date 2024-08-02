package com.ai.aichatbackend.utils;

import com.ai.aichatbackend.common.Constants.AIChatConstants;
import com.ai.aichatbackend.domain.ApiParams;
import org.springframework.stereotype.Component;
import java.io.*;

/**
 * @author mac
 */
@Component
public class ApiParamsUtils {
    /**
     * 将 ApiParams 对象保存到本地文件
     * @param params ApiParams 对象
     * @throws IOException 如果发生 I/O 错误
     */
    public void saveApiParams(ApiParams params) throws IOException {
        File file = new File(AIChatConstants.PARAMS_FILE);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
            }catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Failed to create directory for config file", e);
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(params);
        }
    }

    /**
     * 从本地文件读取 ApiParams 对象，如果文件不存在则创建默认参数
     * @return ApiParams 对象
     * @throws IOException 如果发生 I/O 错误
     * @throws ClassNotFoundException 如果类不存在
     */
    public ApiParams loadApiParams() throws IOException, ClassNotFoundException {
        File file = new File(AIChatConstants.PARAMS_FILE);
        if (!file.exists()) {
            ApiParams defaultParams = createDefaultParams();
            saveApiParams(defaultParams);
            return defaultParams;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ApiParams) ois.readObject();
        }
    }

    /**
     * 创建默认的 ApiParams 对象
     * @return 默认的 ApiParams 对象
     */
    private ApiParams createDefaultParams() {
        ApiParams defaultParams = new ApiParams();
        defaultParams.setTopP(0.8);
        defaultParams.setTopK(0);
        defaultParams.setRepetitionPenalty(1.10f);
        defaultParams.setTemperature(0.8f);
        return defaultParams;
    }
}
