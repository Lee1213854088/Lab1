package org.example;


import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Link;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

import java.io.*;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;

public class Main {
    private Map<String, Map<String, Integer>> graph = new HashMap<>();
    private Map<String, String> predecessors = new HashMap<>();
    private Random random = new Random();
    private Map<String, MutableNode> createdNodes = new HashMap<>();


    // extra 2
    public Map<String, List<String>> getAllShortestPathsFrom(String source) {
        Map<String, List<String>> allPaths = new HashMap<>();
        // 对于图中的每一个顶点，如果顶点不是源点，则计算从源点到该顶点的最短路径
        for (String vertex : graph.keySet()) {
            if (!vertex.equals(source)) {
                List<String> path = getShortestPath(source, vertex);
                if (!path.isEmpty()) {
                    allPaths.put(vertex, path);
                }
            }
        }
        return allPaths;
    }

    // require 6
    public void randomWalk() {
        // 随机选择一个起点
        List<String> vertices = new ArrayList<>(graph.keySet());
        String current = vertices.get(random.nextInt(vertices.size()));

        Set<String> visitedEdges = new HashSet<>();
        List<String> path = new ArrayList<>();
        path.add(current);

        // 遍历图
        while (true) {
            Map<String, Integer> edges = graph.get(current);

            // 如果当前节点没有出边或用户选择停止，退出遍历
            if (edges == null || edges.isEmpty() || Thread.interrupted()) {
                break;
            }

            List<String> neighbors = new ArrayList<>(edges.keySet());
            String next = neighbors.get(random.nextInt(neighbors.size()));
            String edge = current + "→" + next;

            // 检查边是否已经访问过
            if (visitedEdges.contains(edge)) {
                break;
            }

            visitedEdges.add(edge);
            path.add(next);
            current = next;
        }

        // 输出遍历的节点到文本文件
        try {
            writePathToFile(path);
        } catch (IOException e) {
            System.err.println("Error writing the path to file: " + e.getMessage());
        }
    }

    private void writePathToFile(List<String> path) throws IOException {
        String filename = "Output/random_walk.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String node : path) {
                writer.write(node);
                writer.write(" ");
            }
        }
        System.out.println("Random walk path written to " + filename);
    }


    // require 5
    public Map<String, Integer> dijkstra(String source) {
        if (!graph.containsKey(source)) {
            return new HashMap<>(); // 如果源单词不在图中，返回空的距离映射
        }

        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());
        predecessors.clear(); // 清除前驱节点信息
        Set<String> visited = new HashSet<>();

        for (String vertex : graph.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(source, 0);
        pq.offer(new AbstractMap.SimpleEntry<>(source, 0));

        while (!pq.isEmpty()) {
            String current = pq.poll().getKey();
            if (!visited.add(current)) continue;

            int currentDistance = distances.get(current);
            Map<String, Integer> neighbors = graph.getOrDefault(current, new HashMap<>());
            for (Map.Entry<String, Integer> neighbor : neighbors.entrySet()) {
                String next = neighbor.getKey();
                int weight = neighbor.getValue();
                int totalDistance = currentDistance + weight;
                if (totalDistance < distances.getOrDefault(next, Integer.MAX_VALUE)) {
                    distances.put(next, totalDistance);
                    predecessors.put(next, current);
                    pq.offer(new AbstractMap.SimpleEntry<>(next, totalDistance));
                }
            }
        }
        return distances;
    }

    public List<String> getShortestPath(String source, String target) {
        if (!graph.containsKey(source) || !graph.containsKey(target)) {
            System.out.println("One or both of the words do not exist in the graph.");
            return new LinkedList<>(); // 如果源或目标单词不在图中，返回空路径
        }

        Map<String, Integer> distances = dijkstra(source);
        LinkedList<String> path = new LinkedList<>();
        String step = target;

        if (distances.getOrDefault(step, Integer.MAX_VALUE) == Integer.MAX_VALUE) {
            System.out.println("There is no path between " + source + " and " + target);
            return path; // 如果目标不可达，返回空路径
        }

        path.add(step);
        while (step != null && !step.equals(source)) {
            step = predecessors.get(step); // 安全地获取前驱节点
            if (step != null) {
                path.add(step);
            }
        }
        Collections.reverse(path);
        return path;
    }

    // require 4
    public String generateTextWithBridgeWords(String inputText) {
        StringBuilder newText = new StringBuilder();
        String[] words = inputText.toLowerCase().split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                newText.append(" ");
            }
            newText.append(words[i]);
            if (i < words.length - 1) {
                List<String> bridgeWords = findBridgeWords(words[i], words[i + 1]);
                if (!bridgeWords.isEmpty()) {
                    // 随机选择一个桥接词插入
                    String bridgeWord = bridgeWords.get(random.nextInt(bridgeWords.size()));
                    newText.append(" ").append(bridgeWord);
                }
            }
        }
        return newText.toString();
    }

    // require 3
    public List<String> findBridgeWords(String word1, String word2) {
        List<String> bridgeWords = new ArrayList<>();
        if (graph.containsKey(word1)) {
            Map<String, Integer> edgesFromWord1 = graph.get(word1);
            for (String word3 : edgesFromWord1.keySet()) {
                if (graph.containsKey(word3) && graph.get(word3).containsKey(word2)) {
                    bridgeWords.add(word3);
                }
            }
        }
        return bridgeWords;
    }

    public void queryBridgeWords(String word1, String word2) {
        if (!graph.containsKey(word1) && !graph.containsKey(word2)) {
            System.out.println("Neither " + word1 + " nor " + word2 + " is in the graph!");
        } else if (!graph.containsKey(word1)) {
            System.out.println("No " + word1 + " in the graph!");
        } else if (!graph.containsKey(word2)) {
            System.out.println("No " + word2 + " in the graph!");
        } else {
            List<String> bridgeWords = findBridgeWords(word1, word2);
            if (bridgeWords.isEmpty()) {
                System.out.println("No bridge words from " + word1 + " to " + word2 + "!");
            } else {
                System.out.print("The bridge words from " + word1 + " to " + word2 + " are: ");
                System.out.println(String.join(", ", bridgeWords));
            }
        }
    }


    // require 1 and 2
    public void readFileAndBuildGraph(String filePath) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String previousWord = null;
            while((line = reader.readLine()) != null) {
                // 替换标点和非字母字符为空格，转换为小写
                String[] words = line.toLowerCase().replaceAll("[^a-z\\s]", " ").split("\\s+");
                for(String word : words) {
                    if(!word.isEmpty()) {
                        if(previousWord != null) {
                            addEdge(previousWord, word);
                        }
                        previousWord = word;
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void addEdge(String from, String to) {
        graph.putIfAbsent(from, new HashMap<>());
        Map<String, Integer> edges = graph.get(from);
        edges.put(to, edges.getOrDefault(to, 0) + 1);
    }


    public void printGraph() throws IOException {
        // 创建一个可变的有向图
        MutableGraph g = mutGraph("example").setDirected(true);

        // 遍历图的数据结构，为每个节点和边添加到MutableGraph对象
        for(String from : graph.keySet()) {
            MutableNode fromNode = createdNodes.computeIfAbsent(from, n -> mutNode(from));
            Map<String, Integer> edges = graph.get(from);
            for(String to : edges.keySet()) {
                MutableNode toNode = createdNodes.computeIfAbsent(to, n -> mutNode(to));
                String edgeWeight = String.valueOf(edges.get(to));

                // 创建边并设置颜色和标签
                Link link = to(toNode).with(Label.of(edgeWeight), Color.rgb("9999ff"));
                fromNode.addLink(link);

                System.out.println(from + " -> " + to + " [权重: " + edgeWeight + "]");
            }
            g.add(fromNode);
        }

        // 使用Graphviz将图形渲染为PNG文件
        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("output-graph.png"));
        System.out.println("图形已保存为 output-graph.png");
    }

    public static void main(String[] args) throws Exception{
        Main graphBuilder = new Main();
        graphBuilder.readFileAndBuildGraph("Input/1.txt");
        // 假设已经有了一个方法来构建图: graph.buildGraph();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请选择一个操作：");
            System.out.println("1 - 随机游走");
            System.out.println("2 - 计算两个单词之间的最短路径");
            System.out.println("3 - 根据bridge word生成新文本");
            System.out.println("4 - 展示有向图");
            System.out.println("5 - 查询bridge word");
            System.out.println("0 - 退出");
            System.out.print("输入选项：");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消费掉nextInt后的换行符
            switch (choice) {
                case 1:
                    graphBuilder.randomWalk();
                    break;
                case 2:
                    System.out.print("Enter the source word (leave blank to calculate from one word to all others): ");
                    String source = scanner.nextLine().toLowerCase();
                    if (source.isEmpty()) {
                        System.out.println("You must enter at least one word.");
                        break;
                    }

                    System.out.print("Enter the target word (optional, leave blank to find paths to all words): ");
                    String target = scanner.nextLine().toLowerCase();

                    if (!target.isEmpty()) {
                        // 用户输入了两个单词，计算两个单词之间的最短路径
                        List<String> path = graphBuilder.getShortestPath(source, target);
                        if (path.isEmpty()) {
                            System.out.println(source + " and " + target + " are not reachable.");
                        } else {
                            System.out.println("The shortest path from " + source + " to " + target + " is: ");
                            System.out.println(String.join(" → ", path));
                        }
                    } else {
                        // 用户只输入了一个单词，计算该单词到图中其他任一单词的最短路径
                        Map<String, List<String>> allPaths = graphBuilder.getAllShortestPathsFrom(source);
                        if (allPaths.isEmpty()) {
                            System.out.println("No paths found from " + source);
                        } else {
                            for (Map.Entry<String, List<String>> entry : allPaths.entrySet()) {
                                System.out.println("The shortest path from " + source + " to " + entry.getKey() + " is: ");
                                System.out.println(String.join(" → ", entry.getValue()));
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Enter a line of text:");
                    String inputText = scanner.nextLine();

                    String newText = graphBuilder.generateTextWithBridgeWords(inputText);
                    System.out.println("New text with bridge words:");
                    System.out.println(newText);
                    break;
                case 4:
                    graphBuilder.printGraph();
                    break;
                case 5:
                    System.out.print("Enter word1: ");
                    String word1 = scanner.nextLine().toLowerCase();
                    System.out.print("Enter word2: ");
                    String word2 = scanner.nextLine().toLowerCase();
                    graphBuilder.queryBridgeWords(word1, word2);
                    break;
                case 0:
                    System.out.println("退出程序...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效的选项，请重新选择！");
                    break;
            }
        }
    }
}