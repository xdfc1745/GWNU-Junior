#include <iostream>
#include <iomanip>
#include <algorithm>
#define MAX 50

using namespace std;

int W[] = { 2, 4, 5, 8, 3 };
int P[] = { 4, 6, 8, 9, 6 };
int cnt = 0;

typedef struct node { // �迭�� ��ġ�� ��Ÿ���� �������� ����ü
	int i;
	int w;
}NODE;
NODE nd[MAX*MAX];

int max(int a, int b) { // ū ���� ����
	if (a > b) return a;
	else return b;
}

bool cmp(const NODE &p1, const NODE &p2) { // ����ü�� �� �Լ�
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

int ks_DP_1(int n, int M, int k[MAX][MAX]) { // ������ȹ�� 1
	int i, w;

	for (w = 0; w <= M; w++) k[0][w] = 0;
	for (i = 0; i <= n; i++) k[i][0] = 0; // ù��° �� 0���� �ʱ�ȭ

	for (i = 1; i <= n; i++)
		for (w = 1; w <= M; w++)
			if (w < W[i]) k[i][w] = k[i - 1][w];
			else k[i][w] = max(k[i - 1][w], k[i - 1][w - W[i]] + P[i]);
	return k[n][M];
}

int find_fair(int i, int w) { // �ʿ��� ��ġ ����
	int j;
	for (j = 0; j < cnt; j++) { //���� ���� ������ �Է� x
		if (nd[j].i == i && nd[j].w == w) break;
	}
	if (j == cnt) { // �迭�� ���� ���� ������ �Է�
		nd[cnt].i = i;
		nd[cnt].w = w;
		cnt++;
	}
	//cout << "(" << i << " , " << w << ")" << endl;
	if (i == 0 || w == 0) return 0;
	if (w < W[i]) return find_fair(i - 1, w);
	return max(find_fair(i - 1, w), find_fair(i - 1, w - W[i]) + P[i]); // ���������� ���� �迭�� ��ġ�� ����
}

int ks_DP_2(int n, int M, int k[MAX][MAX]) { // ������ ��ġ�� ���� ������ �賶���� �ذ�
	int i, w;

	for (i = 0; i <= n; i++) { // �ʱ�ȭ. ��� �迭�� ���� 0���� 
		for (w = 0; w <= M; w++)
			k[i][w] = 0;
	}

	sort(nd, nd + cnt, cmp); // ��ġ�� ��Ÿ���� �迭�� ������������ ����

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
		if (i == 0) { //i�� 0�϶� �ʱ�ȭ 
			x[i][i] = 0; y[i][i] = 0;
			spX[i][i] = W[i]; spY[i][i] = P[i];
		}
		else {
			for (int j = 0;; j++) {
				// 0�� �ƴ϶�� ���� S[i-1]���� S[i]�� ����
				if ((x[i - 1][j] >= 0) && (y[i - 1][j] >= 0)) { // ��ȿ�� �����ִ��� �˻�
					x[i][j] = x[i - 1][j]; y[i][j] = y[i - 1][j];
				}
				else break; // ��ȿ�� ���� ���ٸ� for�� Ż��
			}
			for (int j = 0;; j++) {
				if ((spX[i - 1][j] >= 0) && (spY[i - 1][j] >= 0)) {
					// SPi-1�� ��ȿ�Ѱ��� ����ִٸ�
					for (int k = 0; k <= 19; k++) {
						if ((x[i][k] < spX[i - 1][j]) && (y[i][k] > spY[i - 1][j])) break;
						//(x2>=x1 && y2<=y1)
						if ((x[i][k] >= spX[i - 1][j]) && (y[i][k] <= spY[i - 1][j])) {
							//(x1>=x2 && y1<=y2)
							x[i][k] = spX[i - 1][j]; y[i][k] = spY[i - 1][j];
							//S(x,y)������ SP(x-1,y-1)�� �ٲ��ش�.
							break;
						}
						if (x[i][k] < 0) {
							//S(x,y)��κп� SP(x,y)ä���
							x[i][k] = spX[i - 1][j]; y[i][k] = spY[i - 1][j];
							break;
						}
					}
				}
				else break;
			}
			if (i != n) { //SP�� S���� ����� 1�۴�
				for (int j = 0;; j++) {
					if (x[i][j] >= 0) {
						//SP(x,y)=S(x+W[i],y+P[i]) ����
						spX[i][j] = x[i][j] + W[i]; spY[i][j] = y[i][j] + P[i];
					}
					else break;
				}
			}
		}
	}
	for (int i = 0; i < 10; i++) { //������� ����
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
	for (int i = 0; i < 10; i++) {  //���� �κ� ��� -1�� �ʱ�ȭ
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
	for (int i = 0; i <= n; i++) { //���� ȿ�������� �ִ� ������ ��
		for (int j = 0; j < 20; j++) {
			if (x[i][j] <= M)
				if (y[i][j] >= result) {
					result = y[i][j];
				}
		}
	}
	for (int i = 0; i <= n; i++) { //��¹�
		cout << "x, y[" << i << "] : ";
		for (int j = 0; j < 20; j++) {
			if (x[i][j] >= 0 && y[i][j] >= 0)
				cout <<" (" <<  x[i][j] << ", " << y[i][j] << ")  ";
		}
		cout << endl;
	}
	/*for (int i = 0; i <= n; i++) { //��¹�
		cout << "Sy[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (y[i][j] >= 0)
				cout << y[i][j] << " ";
		}
		cout << endl;
	}*/
	for (int i = 0; i < n; i++) { //��¹�
		cout << "spX, spY[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (spX[i][j] >= 0 && spY[i][j] >= 0)
				cout << " (" << spX[i][j] << ", " << spY[i][j] << ")  ";
		}
		cout << endl;
	}
	/*for (int i = 0; i < n; i++) { //��¹�
		cout << "SPy[" << i << "] :";
		for (int j = 0; j < 20; j++) {
			if (spY[i][j] >= 0)
				cout << spY[i][j] << " ";
		}
		cout << endl;
	}*/
	return result; //�ִ밪
}


int main() {
	int result1[MAX][MAX], result2[MAX][MAX], total1, num, total2, result3;

	total1 = ks_DP_1(5, 13, result1); // ������ȹ�� 1

	for (int i = 0; i <= 5; i++) { // ������ȹ�� 1�� �� �迭 ���
		for (int j = 0; j <= 13; j++)
			cout << setw(4) << result1[i][j];
		cout << endl;
	}
	num = find_fair(5, 13); // �迭�� ��ġ ����
	total2 = ks_DP_2(5, 13, result2); // ������ȹ��2 ����

	cout << endl << endl << "������ȹ�� 2" << endl;
	for (int i = 0; i <= 5; i++) { // ������ȹ��2 �� �迭 ���
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