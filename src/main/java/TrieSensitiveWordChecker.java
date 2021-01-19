import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TrieSensitiveWordChecker {

    private TrieNode rootNode = null;

    public void initTrie(String sensitiveWordsFilePath) {
        rootNode = new TrieNode();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sensitiveWordsFilePath)) {
            if (is == null) {
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String lintText;
            while ((lintText = reader.readLine()) != null){
                addWord(lintText.trim());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 过滤器，输入原始文本，将文本中的敏感词替换为"***"，返回处理后的文本
     * @param text - 输入文本
     * @return 将输入文本中的敏感词进行替换后的结果
     */
    public String filter(String text){
        if (rootNode == null) {
            System.err.println("请先初始化敏感词过滤器");
            return null;
        }
        if(StringUtils.isEmpty(text)){
            return text;
        }
        StringBuilder sb = new StringBuilder();
        String replacement = "***";
        int begin = 0;
        int position = 0;
        TrieNode tempNode = rootNode;

        //对文本的每个字符进行遍历
        while (position<text.length()){
            Character c = text.charAt(position);

            //如果当前字符是干扰符号，则直接跳过；
            // 如果字典树遍历还未开始则将头指针begin++，并且将当前字符添加到暂存区sb中
            if(isSymbol(c)){
                position++;
                if(tempNode==rootNode){
                    sb.append(c);
                    begin++;
                }
                continue;
            }

            TrieNode subNode;
            if(null != (subNode = tempNode.getSubNode(c))){
                //如果当前Node下面包含有c字符对应的Node，则将position指针移动一位
                tempNode = subNode;
                position++;
                //如果包含c字符的subNode已经是结尾Node，则表示begin到position的字符串是敏感词，需要替换为"***"
                //同时将字典树的指针tempNode回归到根结点
                if(subNode.isKeyWordEnd()){
                    sb.append(replacement);
                    tempNode = rootNode;
                    begin = position;
                }
                if(position == text.length()){
                    for(int i = begin; i<position; i++){
                        sb.append(text.charAt(i));
                    }
                    break;
                }
            }else {
                //如果当前Node下不包含c字符对应的Node，则position指针移动一位，并将begin与position同步
                //同时将字典树的指针tempNode回归到根结点
                //将begin到position之间的字符串添加到暂存区sb
                position++;
                for(int i = begin; i<position; i++){
                    sb.append(text.charAt(i));
                }
                begin = position;
                tempNode = rootNode;
            }
        }
        return sb.toString();
    }

    /**
     * 对每一行敏感词进行遍历，将该关键字的每个字添加到字典树的每个结点上
     * @param lineText - 敏感词表中的其中一行（即单个敏感词）
     */
    private void addWord(String lineText){
        TrieNode tempNode = rootNode;
        for (char c : lineText.toCharArray()) {
            TrieNode subNode = tempNode.getSubNode(c);
            if(Objects.isNull(subNode)){
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;
        }
        //字符串遍历结束，将最后节点标记为尾部节点
        tempNode.setKeyWordEnd();
    }

    //判断字符是否为特殊符号
    private boolean isSymbol(char c){
        return !CharUtils.isAsciiNumeric(c) && ((int) c <0x2E80 || (int) c >0x9FFF);
    }

    private static class TrieNode{
        private boolean end = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();
        private void addSubNode(Character key, TrieNode node){
            subNodes.put(key, node);
        }

        private TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        private boolean isKeyWordEnd(){
            return end;
        }

        private void setKeyWordEnd() {
            this.end = true;
        }
    }
}
