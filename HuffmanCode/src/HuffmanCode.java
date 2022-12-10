
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// Düğüm oluşturabileceğimiz bir sınıf oluşturuyoruz.
class Node {

    Character ch;
    Integer freq;
    Node left = null;
    Node right = null;

    // Burada constructorlarımızı tanımlıyoruz.
    Node(Character ch, Integer freq) {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(Character ch, Integer freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

public class HuffmanCode {
    //Huffman işlevlerini tanımlıyoruz.

    public static void HuffmanOlustur(String text) {
        // İlk durum : liste boşsa;
        if (text == null || text.length() == 0) {
            return;
        }

        // Her karakterin görülme sıklığını bir mapte saklıyoruz.
        Map<Character, Integer> freq = new HashMap<>();

        //Burada döngü ile metni karakter dizisine çeviriyoruz.
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // Huffmanda yüksek önceliğe göre burada bir kuyruk oluşturuyoruz.
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));
        for (var entry : freq.entrySet()) {

            pq.add(new Node(entry.getKey(), entry.getValue()));
        }
        //While döngüsü kuyrukta birden fazla düğüm olana kadar çalışır. 

        while (pq.size() != 1) {
            //Sıradan en yüksek önceliğe göre düğümleri kaldırıyoruz. 
            Node left = pq.poll();
            Node right = pq.poll();

            //Burada iç düğüm oluşturuyoruz. 
            int sum = left.freq + right.freq;

            pq.add(new Node(null, sum, left, right));
        }
        //Root işaretçiyi Huffman'ın kök düğümünde saklar.
        Node root = pq.peek();

        Map<Character, String> huffmanCode = new HashMap<>();
        encodeData(root, "", huffmanCode);

        System.out.println("Karakterlerin Huffan Kodları:  " + huffmanCode);

        StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {
            // Karakterleri alarak kodlanmış diziyi yazdırıyoruz.
            sb.append(huffmanCode.get(c));
        }
        
        System.out.println("Kodlanmış dizi: " + sb);
        System.out.print("Kodlanan dizi: ");
        
        if (isLeaf(root)) {
            //Özel durumları tanımlıyoruz. 
            while (root.freq-- > 0) {
                System.out.print(root.ch);
            }
        } else {
            //Kodlanmış dizinin kodlarını çözüyoruz.
            int index = -1;
            while (index < sb.length() - 1) {
                index = decodeData(root, index, sb);
            }
        }
    }

    //Verileri kodlayan fonsiyonumuzu oluşturuyoruz.
    public static void encodeData(Node root, String str, Map<Character, String> huffmanCode) {
        if (root == null) {
            return;
        }
        if (isLeaf(root)) {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }
        encodeData(root.left, str + '0', huffmanCode);
        encodeData(root.right, str + '1', huffmanCode);
    }

    public static int decodeData(Node root, int index, StringBuilder sb) {

        if (root == null) {
            return index;
        }

        if (isLeaf(root)) {
            System.out.print(root.ch);
            return index;
        }
        index++;
        root = (sb.charAt(index) == '0') ? root.left : root.right;
        index = decodeData(root, index, sb);
        return index;
    }

    //Huffman tek düğüm içeriyorsa;
    public static boolean isLeaf(Node root) {
        return root.left == null && root.right == null;
    }

    public static void main(String args[]) {
        String text = "ABBCAACABBDDACBADEABCDAABAADEB";

        HuffmanOlustur(text);
    }
}
