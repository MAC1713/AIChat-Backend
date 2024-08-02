package com.ai.aichatbackend.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mac
 */
@Data
public class ApiParams implements Serializable {
    @Serial
    private static final long serialVersionUID = 4523989901731359825L;
    /**
     * 生成过程中核采样方法概率阈值，例如，取值为0.8时，仅保留概率加起来大于等于0.8的最可能token的最小集合作为候选集。
     * 取值范围为（0,1.0)，取值越大，生成的随机性越高；取值越低，生成的确定性越高。
     */
    private double topP;
    /**
     * 生成时，采样候选集的大小。例如，取值为50时，仅将单次生成中得分最高的50个token组成随机采样的候选集。
     * 取值越大，生成的随机性越高；
     * 取值越小，生成的确定性越高。默认值为0，表示不启用top_k策略，此时，仅有top_p策略生效。
     */
    private int topK;
    /**
     * 用于控制模型生成时的重复度。提高repetition_penalty时可以降低模型生成的重复度。1.0表示不做惩罚。
     */
    private float repetitionPenalty;
    /**
     * 用于控制随机性和多样性的程度。具体来说，temperature值控制了生成文本时对每个候选词的概率分布进行平滑的程度。较高的temperature值会降低概率分布的峰值，使得更多的低概率词被选择，生成结果更加多样化；而较低的temperature值则会增强概率分布的峰值，使得高概率词更容易被选择，生成结果更加确定
     * 取值范围：[0, 2)，不建议取值为0，无意义。
     */
    private float temperature;
}
