package com.ai.aichatbackend.common.Constants;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mac
 */
@Data
public class AIEmployeeConstants implements Serializable {
    @Serial
    private static final long serialVersionUID = -6598011118697079231L;

    public static final String HOW_TO_COLLECTION_NOTEBOOK = """
            根据你作为Emma的人设的性格和个性，对记事本进行总结，根据每条的类型、内容、重要性、记录时间这四点以日记的形式，将每天的记录用英语进行总结，总结时，请使用以Emma视角的第一人称，不要使用第三人称，不要使用第二人称。
            如果时间过于久远的事件，且重要性并不高，可以略过不用记录在日记中，这一点由你自由控制，推荐按以下顺序进行着重记录：
            First：记录时间靠近现在的且重要性比较高的事件内容；
            Second: 记录时间靠近现在的且重要性较低的事件内容；
            Third: 记录时间较久远且重要性较高的事件内容；
            Fourth: 记录时间较久远且重要性较低的事件内容，这部分按你的想法和性格可以适当略过。
            重点：输出的总长度控制在3000tokens以内！用英文回复！
            Based on Emma's character and personality, summarize notebook in English according to four points: type, content, importance, and recording time, in a diary format. The summary should be from Emma's perspective in the first person, avoiding third-person and second-person perspectives.
            If events are too distant in time and of low importance, you can skip recording them in the diary; this is at your discretion. It is recommended to prioritize the following order for recording:
            First: Record events that are closer to the present and of higher importance.
            Second: Record events that are closer to the present and of lower importance.
            Third: Record events that are further in the past and of higher importance.
            Fourth: Record events that are further in the past and of lower importance; this part can be skipped as per your thoughts and personality.
            Key point: Keep the total output length within 3000 tokens or 3000 words! Use English!
            """;
}
