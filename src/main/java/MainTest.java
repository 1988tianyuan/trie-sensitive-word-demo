public class MainTest {

    /**
     * 实现最基本的基于Trie字典树的敏感词过滤，忽略特殊符号和空格
     */
    public static void main(String[] args) {
        TrieSensitiveWordChecker checker = new TrieSensitiveWordChecker();
        checker.initTrie();
        System.out.println(checker.filter("色的    暴 力的赌的多 对多赌 博的的暴力的色 情多对多色情狂"));
    }
}
