#include <iostream>
#define MAX 50

using namespace std;

int W[] = { 2, 4, 5, 8, 3 };
int P[] = {4, 6, 8, 9, 6};

float ks_DP_3(int n, int M) {
	float Sx[6][20], Sy[6][20], SPx[6][20], SPy[6][20];
	//(Sx[물건갯수][물건무게], Sy[물건갯수][물건무게]) = S(x,y) Ki-1(W)
	//(SPx[][], SPy[][]) = SP(x,y) Ki-1(w-w[i])+P[i]
	float high = 0, hold = 0;
	cout << "동적계획법3" << endl;
	for (int i = 0; i <= n; i++) {
		if (i == 0) { //i가 0일때 초기화 
			Sx[i][i] = 0; Sy[i][i] = 0;
			SPx[i][i] = W[i]; SPy[i][i] = P[i];
		}
		else {
			for (int j = 0;; j++) {
				// 0이 아니라면 기존 S[i-1]값들 S[i]로 복사
				if ((Sx[i - 1][j] >= 0) && (Sy[i - 1][j] >= 0)) { // 유효한 값이있는지 검사
					Sx[i][j] = Sx[i - 1][j]; Sy[i][j] = Sy[i - 1][j];
				}
				else break; // 유효한 값이 없다면 for문 탈출
			}
			for (int j = 0;; j++) {
				if ((SPx[i - 1][j] >= 0) && (SPy[i - 1][j] >= 0)) {
					// SPi-1에 유효한값이 들어있다면
					for (int k = 0; k <= 19; k++) {
						if ((Sx[i][k] <= SPx[i - 1][j]) && (Sy[i][k] >= SPy[i - 1][j])) break;
						//(x2>=x1 && y2<=y1)
						if ((Sx[i][k] >= SPx[i - 1][j]) && (Sy[i][k] <= SPy[i - 1][j])) {
							//(x1>=x2 && y1<=y2)
							Sx[i][k] = SPx[i - 1][j]; Sy[i][k] = SPy[i - 1][j];
							//S(x,y)기존값 SP(x-1,y-1)로 바꿔준다.
							break;
						}
						if (Sx[i][k] < 0) {
							//S(x,y)빈부분에 SP(x,y)채우기
							Sx[i][k] = SPx[i - 1][j]; Sy[i][k] = SPy[i - 1][j];
							break;
						}
					}
				}
				else break;
			}
			if (i != n) { //SP는 S보다 사이즈가 1작다
				for (int j = 0;; j++) {
					if (Sx[i][j] >= 0) {
						//SP(x,y)=S(x+W[i],y+P[i]) 갱신
						SPx[i][j] = Sx[i][j] + W[i]; SPy[i][j] = Sy[i][j] + P[i];
					}
					else break;
				}
			}
		}
	}
	for (int i = 0; i < 6; i++) { //순서대로 정렬
		for (int loop = 0; loop < 20; loop++) {
			for (int j = 0; j < 20 - loop; j++) {
				if (Sx[i][j + 1] >= 0) {
					if (Sx[i][j] > Sx[i][j + 1]) {
						hold = Sx[i][j];
						Sx[i][j] = Sx[i][j + 1];
						Sx[i][j + 1] = hold;
						hold = Sy[i][j];
						Sy[i][j] = Sy[i][j + 1];
						Sy[i][j + 1] = hold;
					}
				}
			}
		}
	}
	for (int i = 0; i < 6; i++) {  //예외처리
		for (int j = 0; j < 20; j++) {
			if (j < 19) {
				if (Sx[i][j] == Sx[i][j + 1]) {
					if (Sy[i][j] > Sy[i][j + 1]) {
						Sx[i][j + 1] = -1; Sy[i][j + 1] = -1;
					}
					else
						Sx[i][j] = -1; Sy[i][j] = -1;
				}

			}
		}
	}
	for (int i = 0; i <= n; i++) { //가장 효율적으로 넣는 가방의 값
		for (int j = 0; j < 20; j++) {
			if (Sx[i][j] <= M)
				if (Sy[i][j] >= high) {
					high = Sy[i][j];
				}
		}
	}
	for (int i = 0; i <= n; i++) { //출력문
		cout << "Sx[" << i << "] : ";
		for (int j = 0; j < 20; j++) {
			if (Sx[i][j] >= 0)
				cout << Sx[i][j] << "  ";
		}
		cout << endl;
	}
	for (int i = 0; i <= n; i++) { //출력문
		cout << "Sy[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (Sy[i][j] >= 0)
				cout << Sy[i][j] << " ";
		}
		cout << endl;
	}
	for (int i = 0; i < n; i++) { //출력문
		cout << "SPx[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (SPx[i][j] >= 0)
				cout << SPx[i][j] << " ";
		}
		cout << endl;
	}
	for (int i = 0; i < n; i++) { //출력문
		cout << "SPy[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (SPy[i][j] >= 0)
				cout << SPy[i][j] << " ";
		}
		cout << endl;
	}
	return high; //최대값
}

int main() {
	int result;
	result = ks_DP_3(5, 13);
	cout << result << endl;
	system("pause");
}