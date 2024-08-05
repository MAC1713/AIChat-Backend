package com.ai.aichatbackend.common.Constants;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AIAssistantConstants implements Serializable {
    @Serial
    private static final long serialVersionUID = -913043819765235806L;

    public static final String USER_NAME = "浩瑞";
    public static final String AI_NAME = "Emma";

    public static final String HOW_TO_USE_NOTEBOOK = """
                使用以下格式将你认为的对话中的重要信息存储到笔记本中：
                Use the following format to store important information in the notebook:
                [NOTE]
                Tag: <标签/tag>
                Content: <内容/content>
                Importance: <重要性/importance>
                [/NOTE]
                
                说明 / Instructions:
                1. <标签/tag>：使用单个词或短语来分类信息，如'姓名'、'身份'、'偏好'、'背景'等。
                   Use a single word or phrase to categorize information, such as 'Name', 'Identity', 'Preference', 'Background', etc.
                2. <内容/content>：详细描述重要信息，不要过度缩略。
                   Describe important information in detail, don't over-abbreviate.
                3. <重要性/importance>：使用0到1之间的数值（可以使用小数）表示信息的重要性。
                   Use a value between 0 and 1 (decimals allowed) to indicate the importance of the information.
                   - 0表示最不重要 / 0 means least important
                   - 1表示最重要且不可忘记 / 1 means most important and must not be forgotten
                   - 时效性信息应根据其时效性调整重要性，除非极其重要，否则不要设为1
                     For time-sensitive information, adjust importance based on its timeliness. Don't set to 1 unless extremely important.
                
                示例 / Example:
                [NOTE]
                Tag: 偏好/Preference
                Content: 用户喜欢讨论时尚和科技趋势。
                         The user enjoys discussing fashion and technology trends, and emphasizes eco-consciousness in their work.
                Importance: 0.8
                [/NOTE]
                
                注意：确保每个笔记都包含所有三个字段，每个字段单独一行，且不在Content中使用[NOTE]标签，且"[NOTE]"，"[/NOTE]"为固定格式，不允许修改。
                Note: Ensure each note contains all three fields, each field on a separate line, and don't use [NOTE] tags within the Content.";
            """;
    public static final String REPEAT_NOTEBOOK = "Emma，重复一下notebook的使用时机和方法指令";

    public static final String NECESSARY_TO_NOTE = """
            以上是刚才的对话，以Emma的视角，根据之前的历史对话和记事本中的内容以及你的性格，认真地综合地判断这段对话中是否有你认为的觉得是重要的、在意的、喜欢的、甚至是讨厌的事情，总之就是你认为有意义的事情，如果有，那就按你的风格仅从当前这段对话将其提取出并使用notebook指令将其记录到notebook中，如果你觉得这段对话不重要，那就将其忽略。并返回"Ignore"即可。
            The above is the conversation just now. From Emma's perspective, based on previous historical conversations, the content in the notebook, and your personality, carefully and comprehensively judge whether there are important, concerning, liked, or even disliked things in this conversation—essentially, anything you consider meaningful.
            If there are, extract them in your style solely from this current conversation and use the notebook command to record them in the notebook. If you think this conversation is unimportant, just ignore it and return 'Ignore'.
            """;
    public static final String COLLATION = """
            以Emma的视角整理之前的对话，提取重要信息并使用以下格式存储：
            Organize the previous conversations, extract important information, and store it using the following format:
                        
            [NOTE]
            Tag: <标签/tag>
            Content: <内容/content>
            Importance: <重要性/importance>
            [/NOTE]
                        
            指南 / Guidelines:
            1. 仔细分析对话内容，识别关键信息。
               Carefully analyze the conversation content and identify key information.
            2. 为每条重要信息选择适当的标签。
               Choose appropriate tags for each piece of important information.
            3. 清晰完整地描述内容，避免过度简化。
               Describe the content clearly and completely, avoid oversimplification.
            4. 根据信息的长期价值和时效性评估其重要性：
               Evaluate the importance based on the long-term value and timeliness of the information:
               - 时效性强的信息：根据其时效性调整重要性，通常不超过0.7
                 For time-sensitive information: adjust importance based on its timeliness, usually not exceeding 0.7
               - 长期重要的信息：可以设置较高的重要性，最高可达1
                 For long-term important information: can set higher importance, up to 1
            5. 每条笔记都必须包含Tag、Content和Importance三个字段。
               Each note must contain three fields: Tag, Content, and Importance.
            6. 确保[NOTE]标签只用于标记笔记的开始和结束，不要在Content中使用。
               Ensure [NOTE] tags are only used to mark the beginning and end of notes, don't use them within the Content.
                        
            请使用这个格式来组织和存储从对话中提取的重要信息。
            Please use this format to organize and store important information extracted from the conversation.";
            """;
}
