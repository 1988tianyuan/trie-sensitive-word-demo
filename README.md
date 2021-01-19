# trie-sensitive-word-demo
基于Trie字典树的敏感词过滤器，可以删选匹配任意长度文本中的敏感词，敏感词列表需要以文件方式提供

### example
0. 提前准备敏感词列表`SensitiveWords.txt`：
```
色情
赌博
嫖娼
吸毒
性交
暴力

```
1. 初始化敏感词过滤器：
```java
TrieSensitiveWordChecker checker = new TrieSensitiveWordChecker();
checker.initTrie("SensitiveWords.txt");
```
2. 进行文本过滤
```java
String result = checker.filter("色的    暴 力的赌的多 对多赌 博的的暴力的色 情多对多色情狂");
```
3. 得到输出result，可以看到，过滤器对该段文本存在于敏感词列表中的敏感词进行了过滤和替换：
```
色的    ***的赌的多 对多***的的***的***多对多***狂
```

**可以基于这个demo作一些二次开发，比如检查是否存在敏感词，获取查找敏感词位置等等**
