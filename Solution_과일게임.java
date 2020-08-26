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

//20200825
//20200825 2번째 수정
//20200826
public class Solution_과일게임 {
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

			//DP를 위한 변수
			long[] DP = new long[W+1];
			//다음값을 저장하기 위한 변수(DP)
			long[] NEXT_DP = new long[W+1];

			for (int i = 0; i < N; i++) {

				st = new StringTokenizer(br.readLine());
				int x = Integer.parseInt(st.nextToken());
				long y = Long.parseLong(st.nextToken());
				long v = Long.parseLong(st.nextToken());

				//(*중요) 과일을 입력받을때, HashMap에 저장하는 함수를 호출한다.
				addFluit(new Fruit(x, y, v));
			}

			//KEY값을 낮은 순서 부터 정렬한다 (TreeSet이용)
			SortedSet<Long> keys = new TreeSet<Long>(fruitList.keySet());

			//현재 높이
			long currentKey = 0;

			//낮은 높이(KEY)부터 순차적으로 검색을 실행한다.
			for (Long key : keys) { 				
				long nextKey = key;

				Arrays.fill(NEXT_DP, 0);

				long diff = nextKey - currentKey; 
				currentKey = nextKey;

				//(*중요) 10^9라는 높이때문에 skip을 위한 구문
				//W값이 넘어가면 해당 높이의 최대값이 모든 칸의 값들에 영향을 끼치기 때문에 해당 로직을 사용한다.
				if(diff >= W) {
					diff = W;
					long max = 0;
					for (int i = 1; i <= W; i++) {
						max = Math.max(max, DP[i]); 
					}

					Arrays.fill(NEXT_DP, max);
					NEXT_DP[0] = 0;
				}else {
					//(*중요) DP
					//해당위치에서 참조가 가능한 최고값을 가져다가 최대값을 저장하는 부분
					for (int i = 1; i <= W; i++) {
						//이전값에서 가져올수 있는 범위를 지정하는 변수 (startI, endI))
						int startI = (int) ((i-diff <= 1) ? 1 : i-diff);
						int endI = (int) ((i+diff >= W) ? W : i+diff);

						//이전값중 최대값 검색(이때 가능한 범위내에서만 for문을 돌린다)
						for (int j = startI; j <= endI; j++) {
							if(DP[j] == 0) {
								continue;
							}
							NEXT_DP[i] = Math.max(NEXT_DP[i], DP[j]);
						}
					}
				}

				//현재높이의 과일의 만족도를 바로 직전까지의 최대의 만족도에다가 각 위치의 만족도들을 더한다 
				ArrayList<Fruit> list = fruitList.get(key);
				for (int i = 0; i < list.size(); i++) {
					NEXT_DP[list.get(i).x] += list.get(i).v;
				}

				DP = NEXT_DP.clone();

			}

			//마지막 위치로 도달하고 난 뒤 최대값을 찾는다
			long maxValue = 0;
			for (int i = 1; i <= W; i++) {
				maxValue = Math.max(maxValue, DP[i]);
			}
			System.out.println("#"+t+ " " + maxValue);
		}
	}

	//(*중요)HashMap에 과일을 저장한다
	//해당함수를 쓰는 이유는 높이를 KEY로 설정하여 같은 높이끼리 값들을 모으고,
	//모은 값으로 낮은 높이부터 계산을 진행할 때, 순차적으로 계산하기 위해 그에 알맞게 해당 값을 저장한다.
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