#include <iostream>
#include <iomanip>
#include <algorithm>
#define MAX 50

using namespace std;

int W[] = { 2, 4, 5, 8, 3 };
int P[] = { 4, 6, 8, 9, 6 };
int cnt = 0;

typedef struct node { // 배열의 위치를 나타내는 순서쌍의 구조체
	int i;
	int w;
}NODE;
NODE nd[MAX*MAX];

int max(int a, int b) { // 큰 값을 리턴
	if (a > b) return a;
	else return b;
}

bool cmp(const NODE &p1, const NODE &p2) { // 구조체의 비교 함수
	if (p1.i < p2.i) {
		return true;
	}
	else if (p1.i == p2.i) {
		return p1.w < p2.w;
	}
	else {
		return false;
	}
}

int ks_DP_1(int n, int M, int k[MAX][MAX]) { // 동적계획법 1
	int i, w;

	for (w = 0; w <= M; w++) k[0][w] = 0;
	for (i = 0; i <= n; i++) k[i][0] = 0; // 첫번째 줄 0으로 초기화

	for (i = 1; i <= n; i++)
		for (w = 1; w <= M; w++)
			if (w < W[i]) k[i][w] = k[i - 1][w];
			else k[i][w] = max(k[i - 1][w], k[i - 1][w - W[i]] + P[i]);
	return k[n][M];
}

int find_fair(int i, int w) { // 필요한 위치 추출
	int j;
	for (j = 0; j < cnt; j++) { //같은 값이 있으면 입력 x
		if (nd[j].i == i && nd[j].w == w) break;
	}
	if (j == cnt) { // 배열에 같은 값이 없으면 입력
		nd[cnt].i = i;
		nd[cnt].w = w;
		cnt++;
	}
	//cout << "(" << i << " , " << w << ")" << endl;
	if (i == 0 || w == 0) return 0;
	if (w < W[i]) return find_fair(i - 1, w);
	return max(find_fair(i - 1, w), find_fair(i - 1, w - W[i]) + P[i]); // 분할정복을 통해 배열의 위치를 얻음
}

int ks_DP_2(int n, int M, int k[MAX][MAX]) { // 추출한 위치에 대한 값으로 배낭문제 해결
	int i, w;

	for (i = 0; i <= n; i++) { // 초기화. 모든 배열의 값을 0으로 
		for (w = 0; w <= M; w++)
			k[i][w] = 0;
	}

	sort(nd, nd + cnt, cmp); // 위치를 나타내는 배열을 오름차순으로 정렬

	for (int j = 0; j < cnt; j++) {
		i = nd[j].i;
		w = nd[j].w;
		if (nd[j].i == 0 || nd[j].w == 0) {
			k[i][w] = 0;
			continue;
		}
		if (w < W[i]) 
			k[i][w] = k[i - 1][w];
		else 
			k[i][w] = max(k[i - 1][w], k[i - 1][w - W[i]] + P[i]);
	}
	return k[n][M];
}

float ks_DP_3(int n, int M) {
	float x[10][20], y[10][20], spX[10][20], spY[10][20];
	//S(x,y) Ki-1(W), (SPx[][], SPy[][]) = Ki-1(w-w[i])+P[i]

	float result = 0, temp = 0;

	for (int i = 0; i <= n; i++) {
		if (i == 0) { //i가 0일때 초기화 
			x[i][i] = 0; y[i][i] = 0;
			spX[i][i] = W[i]; spY[i][i] = P[i];
		}
		else {
			for (int j = 0;; j++) {
				// 0이 아니라면 기존 S[i-1]값들 S[i]로 복사
				if ((x[i - 1][j] >= 0) && (y[i - 1][j] >= 0)) { // 유효한 값이있는지 검사
					x[i][j] = x[i - 1][j]; y[i][j] = y[i - 1][j];
				}
				else break; // 유효한 값이 없다면 for문 탈출
			}
			for (int j = 0;; j++) {
				if ((spX[i - 1][j] >= 0) && (spY[i - 1][j] >= 0)) {
					// SPi-1에 유효한값이 들어있다면
					for (int k = 0; k <= 19; k++) {
						if ((x[i][k] < spX[i - 1][j]) && (y[i][k] > spY[i - 1][j])) break;
						//(x2>=x1 && y2<=y1)
						if ((x[i][k] >= spX[i - 1][j]) && (y[i][k] <= spY[i - 1][j])) {
							//(x1>=x2 && y1<=y2)
							x[i][k] = spX[i - 1][j]; y[i][k] = spY[i - 1][j];
							//S(x,y)기존값 SP(x-1,y-1)로 바꿔준다.
							break;
						}
						if (x[i][k] < 0) {
							//S(x,y)빈부분에 SP(x,y)채우기
							x[i][k] = spX[i - 1][j]; y[i][k] = spY[i - 1][j];
							break;
						}
					}
				}
				else break;
			}
			if (i != n) { //SP는 S보다 사이즈가 1작다
				for (int j = 0;; j++) {
					if (x[i][j] >= 0) {
						//SP(x,y)=S(x+W[i],y+P[i]) 갱신
						spX[i][j] = x[i][j] + W[i]; spY[i][j] = y[i][j] + P[i];
					}
					else break;
				}
			}
		}
	}
	for (int i = 0; i < 10; i++) { //순서대로 정렬
		for (int j = 0; j < 20; j++) {
			for (int k = 0; k < 20 - j; k++) {
				if (x[i][k + 1] >= 0) {
					if (x[i][k] > x[i][k + 1]) {
						temp = x[i][k];
						x[i][k] = x[i][k + 1];
						x[i][k + 1] = temp;
						temp = y[i][k];
						y[i][k] = y[i][k + 1];
						y[i][k + 1] = temp;
					}
				}
			}
		}
	}
	for (int i = 0; i < 10; i++) {  //남은 부분 모두 -1로 초기화
		for (int j = 0; j < 20; j++) {
			//if (j < 19) {
				if (x[i][j] == x[i][j + 1]) {
					if (y[i][j] > y[i][j + 1]) {
						x[i][j + 1] = -1; y[i][j + 1] = -1;
					}
					else
						x[i][j] = -1; y[i][j] = -1;
				}

			//}
		}
	}
	for (int i = 0; i <= n; i++) { //가장 효율적으로 넣는 가방의 값
		for (int j = 0; j < 20; j++) {
			if (x[i][j] <= M)
				if (y[i][j] >= result) {
					result = y[i][j];
				}
		}
	}
	for (int i = 0; i <= n; i++) { //출력문
		cout << "x, y[" << i << "] : ";
		for (int j = 0; j < 20; j++) {
			if (x[i][j] >= 0 && y[i][j] >= 0)
				cout <<" (" <<  x[i][j] << ", " << y[i][j] << ")  ";
		}
		cout << endl;
	}
	/*for (int i = 0; i <= n; i++) { //출력문
		cout << "Sy[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (y[i][j] >= 0)
				cout << y[i][j] << " ";
		}
		cout << endl;
	}*/
	for (int i = 0; i < n; i++) { //출력문
		cout << "spX, spY[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (spX[i][j] >= 0 && spY[i][j] >= 0)
				cout << " (" << spX[i][j] << ", " << spY[i][j] << ")  ";
		}
		cout << endl;
	}
	/*for (int i = 0; i < n; i++) { //출력문
		cout << "SPy[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (spY[i][j] >= 0)
				cout << spY[i][j] << " ";
		}
		cout << endl;
	}*/
	return result; //최대값
}


int main() {
	int result1[MAX][MAX], result2[MAX][MAX], total1, num, total2, result3;

	total1 = ks_DP_1(5, 13, result1); // 동적계획법 1

	for (int i = 0; i <= 5; i++) { // 동적계획법 1한 후 배열 출력
		for (int j = 0; j <= 13; j++)
			cout << setw(4) << result1[i][j];
		cout << endl;
	}
	num = find_fair(5, 13); // 배열의 위치 저장
	total2 = ks_DP_2(5, 13, result2); // 동적계획법2 실행

	cout << endl << endl << "동적계획법 2" << endl;
	for (int i = 0; i <= 5; i++) { // 동적계획법2 후 배열 출력
		for (int j = 0; j <= 13; j++)
			cout << setw(4) << result2[i][j];
		cout << endl;
	}

	result3 = ks_DP_3(5, 13);

	cout << "result1 = " << result1[5][13] << endl;
	cout << "result2 = " << result2[5][13] << endl;
	cout << result3 << endl;
	system("pause");
	
}