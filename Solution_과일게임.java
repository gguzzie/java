package real;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Solution_���ϰ��� {
	static HashMap<Long, ArrayList<Fruit>> fruitList;
	static ArrayList<Long> keyList;

	public static void main(String[] args) throws Exception {
		//			System.setIn(new FileInputStream("Pro_20190406.txt"));

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		int T = Integer.parseInt(br.readLine());

		for (int t = 1; t <= T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int W = Integer.parseInt(st.nextToken());
			long H = Long.parseLong(st.nextToken());
			int N = Integer.parseInt(st.nextToken());

			fruitList = new HashMap<>();
			keyList = new ArrayList<>();

			//DP�� ���� ����
			long[] DP = new long[W+1];
			//�������� �����ϱ� ���� ����(DP)
			long[] NEXT_DP = new long[W+1];

			for (int i = 0; i < N; i++) {

				st = new StringTokenizer(br.readLine());
				int x = Integer.parseInt(st.nextToken());
				long y = Long.parseLong(st.nextToken());
				long v = Long.parseLong(st.nextToken());

				//(*�߿�) ������ �Է¹�����, HashMap�� �����ϴ� �Լ��� ȣ���Ѵ�.
				addFluit(new Fruit(x, y, v));
			}

			//KEY���� ���� ���� ���� �����Ѵ� (TreeSet�̿�)
			SortedSet<Long> keys = new TreeSet<Long>(fruitList.keySet());

			//���� ����
			long currentKey = 0;

			//���� ����(KEY)���� ���������� �˻��� �����Ѵ�.
			for (Long key : keys) { 				
				long nextKey = key;

				Arrays.fill(NEXT_DP, 0);

				long diff = nextKey - currentKey; 
				currentKey = nextKey;

				//(*�߿�) 10^9��� ���̶����� skip�� ���� ����
				//W���� �Ѿ�� �ش� ������ �ִ밪�� ��� ĭ�� ���鿡 ������ ��ġ�� ������ �ش� ������ ����Ѵ�.
				if(diff >= W) {
					diff = W;
					long max = 0;
					for (int i = 1; i <= W; i++) {
						max = Math.max(max, DP[i]); 
					}

					Arrays.fill(NEXT_DP, max);
					NEXT_DP[0] = 0;
				}else {
					//(*�߿�) DP
					//�ش���ġ���� ������ ������ �ְ��� �����ٰ� �ִ밪�� �����ϴ� �κ�
					for (int i = 1; i <= W; i++) {
						//���������� �����ü� �ִ� ������ �����ϴ� ���� (startI, endI))
						int startI = (int) ((i-diff <= 1) ? 1 : i-diff);
						int endI = (int) ((i+diff >= W) ? W : i+diff);

						//�������� �ִ밪 �˻�(�̶� ������ ������������ for���� ������)
						for (int j = startI; j <= endI; j++) {
							if(DP[j] == 0) {
								continue;
							}
							NEXT_DP[i] = Math.max(NEXT_DP[i], DP[j]);
						}
					}
				}

				//��������� ������ �������� �ٷ� ���������� �ִ��� ���������ٰ� �� ��ġ�� ���������� ���Ѵ� 
				ArrayList<Fruit> list = fruitList.get(key);
				for (int i = 0; i < list.size(); i++) {
					NEXT_DP[list.get(i).x] += list.get(i).v;
				}

				DP = NEXT_DP.clone();

			}

			//������ ��ġ�� �����ϰ� �� �� �ִ밪�� ã�´�
			long maxValue = 0;
			for (int i = 1; i <= W; i++) {
				maxValue = Math.max(maxValue, DP[i]);
			}
			System.out.println("#"+t+ " " + maxValue);
		}
	}

	//(*�߿�)HashMap�� ������ �����Ѵ�
	//�ش��Լ��� ���� ������ ���̸� KEY�� �����Ͽ� ���� ���̳��� ������ ������,
	//���� ������ ���� ���̺��� ����� ������ ��, ���������� ����ϱ� ���� �׿� �˸°� �ش� ���� �����Ѵ�.
	static void addFluit(Fruit f) {
		if(fruitList.get(f.y) == null ) {
			ArrayList<Fruit> list = new ArrayList<>();
			list.add(f);
			fruitList.put(f.y, list);
		}else {
			fruitList.get(f.y).add(f);
		}
	}


	public static class Fruit {
		int x;
		long y;
		long v;
		public Fruit(int x2, long y2, long v2) {
			this.x = x2;
			this.y = y2;
			this.v = v2;
		}
		@Override
		public String toString() {
			return "Fruit [x=" + x + ", y=" + y + ", v=" + v + "]";
		}

	}
}