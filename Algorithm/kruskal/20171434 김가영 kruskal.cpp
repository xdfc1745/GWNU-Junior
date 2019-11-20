#include <iostream>
#include <algorithm>
#define MAX 9 // ����� ����
using namespace std;

struct Kruskal { // ����ġ�� �����ϴ� ����ü 
	int start;
	int end;
	int weight;
};

bool cmp_Kruskal(Kruskal a, Kruskal b) { // ����ġ �񱳸� ���� �Լ� 
	return a.weight < b.weight;
}

struct Kruskal k[MAX*MAX]; // ���۳��� �� ���, ����ġ�� �����ϴ� ����ü �迭 ���� 

void init_set(int p[]) { // �迭�� �ʱ�ȭ  
	int i;
	for (i=0; i<9; i++) //�迭�� ��� ���� -1�� �ʱ�ȭ 
		p[i] = -1;	
}

void union_set(int u, int v, int p[]){
	int temp, tempi;
	if (p[u] < 0 && p[v] >= 0) {// v���� �ڽĳ���̰�, u�� ��Ʈ ��Ʈ�� ��� 
		p[u] = v;
		temp = p[v];
		while(1) { // ��Ʈ ��带 ã�� �Ž��� �ö� 
			if (p[temp]>=0) 
				temp = p[temp];
			else {
				p[temp] = p[temp] - 1;
				break;
			}
		}
	}
	else if (p[u] >= 0 && p[v] < 0) { // u���� �ڽĳ���̰�, v�� ��Ʈ ��Ʈ�� ��� 
			p[v] = u;
		temp = p[u];
		while(1) {
			if (p[temp] >=0) 
				temp = p[temp];
			else {  // ��Ʈ ����� ��� ��Ʈ ��忡 -1, �ݺ��� ����  
				p[temp] = p[temp] - 1;
				break;
			}
		}
	}
	else if (p[u] >=0 && p[v] >= 0) { // �� �� �ڽ� ����� ��� ������ �θ� ��带 ã�� �� �� ū Ʈ���� ���� Ʈ���� �� 
		temp = u;
		while (p[temp] >= 0) {
			temp= p[temp];
		}
		tempi = v;
		while (p[tempi] >= 0) {
			tempi = p[tempi];
		}	
		if (p[temp] < p[tempi]){
			p[temp] = p[temp]+ (p[tempi]);	
			p[tempi] = u;
		}
		else {
			p[tempi] = p[temp]+ (p[tempi]);
			p[temp] = v;
		}
	}
	else { // �Ѵ� ��Ʈ �ϰ�� 
		p[v] = u;
		p[u] --;	
	}
	for (int i=0; i<MAX; i++) // Ʈ�� �迭�� ���� ���(Ʈ�� ��� Ȯ�ο�) 
		cout << p[i] << " ";
	cout << endl ;
}

int find_set(int u, int p[]) {
	while (p[u] >= 0) {
			u = p[u];
	}
	return u;
 }
 

int main()
{
	int parent[MAX], ind=0, cnt=0, total=0, i=0;
	int MST_E[MAX]; // �Էµ� ������ �� �迭 
	int weight[MAX][MAX] = {0, 4, 100, 100, 100, 100, 100, 8, 100, 
							4, 0, 8, 100, 100, 100, 100, 11, 100,
							100, 8, 0, 7, 100, 4, 100, 100, 2,
							100, 100, 7, 0, 9, 14, 100, 100, 100,
							100, 100, 100, 9, 0, 10, 100, 100, 100,
							100, 100, 4, 14, 10, 0, 2, 100, 100,
							100, 100, 100, 100, 100, 2, 0, 1, 6,
							8, 11, 100, 100, 100, 100, 1, 0, 7,
							100, 100, 2, 100, 100, 100, 6, 7, 0}; //��� ���� ��� �Է� 
	
	for (int i=0; i<MAX; i++) { // ���������Ŀ��� ����ġ�� �̾Ƽ� ����ü�� ��ġ�� �Բ� ���� 
		for (int j=0; j<i+1; j++) { // ��������� �밢���� �������� ������ ������ �����Ƿ� ���ʸ� ���� 
			if (weight[i][j] != 0 && weight[i][j] != 100) { // �ڱ��ڽ����� ���� ����(0)�� �ƴϰų� ������ ���� ���(100)�� ������ ������ �Է� 
				k[ind].start = i; // ��߳�� 
				k[ind].end = j; // ���� ��� 
				k[ind].weight = weight[i][j]; // ������ ����ġ 
				ind ++; // �� ����ġ ������ ���� 
			}
		}
	}
	
	sort (k, k + ind, cmp_Kruskal); // ����ü�� ����ġ�� ������������ ���� 
	
	for (int i=0; i<ind; i++) {  // ����ġ ����ü�� ��ü ���� ���(���� ���� ���� Ȯ�ο�) 
		cout << k[i].start << ", " << k[i].end << ", " << k[i].weight << endl;
	}
	cout << endl << endl;
	
	init_set(parent); //parent �迭�� ��� ���� -1��, ������ ������ ����
	 
	ind = 0;
	
	while(ind < MAX-1) { // ������ �� -1 ��ŭ �ݺ� 
		if (find_set(k[cnt].start, parent) != find_set(k[cnt].end, parent)) {  // �θ� ��尡 ��ġ�� �� Ȯ��(����Ŭ Ȯ��) 
			MST_E[++ind] = k[cnt].start; 
			union_set(k[cnt].start, k[cnt].end, parent); // �ϳ��� ����(Ʈ��)�� ���� 
			total += k[cnt].weight;
			cout <<"total = " << total; // ���� ���� �� ����ġ �� ��� 
			cout << ", union: " << k[cnt].start << ", " <<  k[cnt].end << ", " << k[cnt].weight<< endl; //  ������ ������ ���� ��� 
			cout << "ind = " << ind << endl<<endl;

		}
		cnt ++;
	} 
	return 0;
}


