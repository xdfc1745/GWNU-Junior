#include <iostream>
#include <algorithm>
#define MAX 9 // 노드의 개수
using namespace std;

struct Kruskal { // 가중치를 저장하는 구조체 
	int start;
	int end;
	int weight;
};

bool cmp_Kruskal(Kruskal a, Kruskal b) { // 가중치 비교를 위한 함수 
	return a.weight < b.weight;
}

struct Kruskal k[MAX*MAX]; // 시작노드와 끝 노드, 가중치를 저장하는 구조체 배열 생성 

void init_set(int p[]) { // 배열을 초기화  
	int i;
	for (i=0; i<9; i++) //배열의 모든 값을 -1로 초기화 
		p[i] = -1;	
}

void union_set(int u, int v, int p[]){
	int temp, tempi;
	if (p[u] < 0 && p[v] >= 0) {// v노드는 자식노드이고, u가 루트 노트일 경우 
		p[u] = v;
		temp = p[v];
		while(1) { // 루트 노드를 찾아 거슬러 올라감 
			if (p[temp]>=0) 
				temp = p[temp];
			else {
				p[temp] = p[temp] - 1;
				break;
			}
		}
	}
	else if (p[u] >= 0 && p[v] < 0) { // u노드는 자식노드이고, v가 루트 노트일 경우 
			p[v] = u;
		temp = p[u];
		while(1) {
			if (p[temp] >=0) 
				temp = p[temp];
			else {  // 루트 노드일 경우 루트 노드에 -1, 반복문 종료  
				p[temp] = p[temp] - 1;
				break;
			}
		}
	}
	else if (p[u] >=0 && p[v] >= 0) { // 둘 다 자식 노드일 경우 각자의 부모 노드를 찾은 후 더 큰 트리로 작은 트리가 들어감 
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
	else { // 둘다 루트 일경우 
		p[v] = u;
		p[u] --;	
	}
	for (int i=0; i<MAX; i++) // 트리 배열의 내용 출력(트리 결과 확인용) 
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
	int MST_E[MAX]; // 입력된 간선의 수 배열 
	int weight[MAX][MAX] = {0, 4, 100, 100, 100, 100, 100, 8, 100, 
							4, 0, 8, 100, 100, 100, 100, 11, 100,
							100, 8, 0, 7, 100, 4, 100, 100, 2,
							100, 100, 7, 0, 9, 14, 100, 100, 100,
							100, 100, 100, 9, 0, 10, 100, 100, 100,
							100, 100, 4, 14, 10, 0, 2, 100, 100,
							100, 100, 100, 100, 100, 2, 0, 1, 6,
							8, 11, 100, 100, 100, 100, 1, 0, 7,
							100, 100, 2, 100, 100, 100, 6, 7, 0}; //비용 인접 행렬 입력 
	
	for (int i=0; i<MAX; i++) { // 비용인접행렬에서 가중치만 뽑아서 구조체에 위치와 함께 저장 
		for (int j=0; j<i+1; j++) { // 인접행렬은 대각서을 기준으로 오른쪽 왼쪽이 같으므로 한쪽만 저장 
			if (weight[i][j] != 0 && weight[i][j] != 100) { // 자기자신으로 가는 간선(0)이 아니거나 간선이 없는 경우(100)을 제외한 나머지 입력 
				k[ind].start = i; // 출발노드 
				k[ind].end = j; // 도착 노드 
				k[ind].weight = weight[i][j]; // 간선의 가중치 
				ind ++; // 총 가중치 간선의 개수 
			}
		}
	}
	
	sort (k, k + ind, cmp_Kruskal); // 구조체의 가중치만 오름차순으로 정렬 
	
	for (int i=0; i<ind; i++) {  // 가중치 구조체의 전체 내용 출력(들어가는 간선 내용 확인용) 
		cout << k[i].start << ", " << k[i].end << ", " << k[i].weight << endl;
	}
	cout << endl << endl;
	
	init_set(parent); //parent 배열의 모든 값을 -1로, 각각의 집합을 생성
	 
	ind = 0;
	
	while(ind < MAX-1) { // 간선의 수 -1 만큼 반복 
		if (find_set(k[cnt].start, parent) != find_set(k[cnt].end, parent)) {  // 부모 노드가 겹치는 지 확인(사이클 확인) 
			MST_E[++ind] = k[cnt].start; 
			union_set(k[cnt].start, k[cnt].end, parent); // 하나의 집합(트리)로 결합 
			total += k[cnt].weight;
			cout <<"total = " << total; // 간선 연결 후 가중치 합 출력 
			cout << ", union: " << k[cnt].start << ", " <<  k[cnt].end << ", " << k[cnt].weight<< endl; //  연결한 간선의 정보 출력 
			cout << "ind = " << ind << endl<<endl;

		}
		cnt ++;
	} 
	return 0;
}


