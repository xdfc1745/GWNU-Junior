#include <iostream>
#include <time.h>
#include <stdlib.h>

using namespace std;

#define MAX 50000 // �迭�� ũ��
#define SEED 3
int sorted[MAX]; // ������ ���� �ӽù迭
void insertionSort(int list[]);
 
void merge(int list[], int left, int mid, int right)
{
    int i, j, k, l;
    i = left; // i�� ���ĵ� ���ʸ���Ʈ�� ���� �ε���
    j = mid + 1; // j�� ���ĵ� �����ʸ���Ʈ�� ���� �ε���
    k = left;  // k�� ���ĵ� ����Ʈ�� ���� �ε���
    
    while (i <= mid && j <= right) {//������ �迭 �պ� 
        if (list[i] <= list[j])
            sorted[k++] = list[i ++];
        else
            sorted[k++] = list[j ++];
    }
    if (i > mid) { // ���� �ִ� ��� ���� �迭�� ����
        for (l = j; l <= right; l ++)
            sorted[k++] = list[l];
    }
    else { 
        for (l = i; l <= mid; l ++)
            sorted[k++] = list[l];
    }
    
    for (l = left; l <= right; l ++) // �ӽ� �迭�� ���ĵ� ���ڵ� ���� �迭�� ���� 
        list[l] = sorted[l];
}

void mergeSort(int list[], int thres, int left, int right) {
    int mid;
 
    if (left < right) {
        mid = (left + right) / 2; 
 
        if (mid > thres) {
            mergeSort(list, thres, left, mid);  // �κи���Ʈ ����
            mergeSort(list, thres, mid + 1, right); //�κи���Ʈ ����
 
            merge(list, left, mid, right);   // �պ�
        }
        else
            insertionSort(list);
    }
}

void quickSort(int list[], int thres, int left, int right) //left = �� �� , right = �� ��
{
    int i = left, j = right;
    int tmp;
    int pivot = list[(left + right) / 2]; // ��� ���� pivot���� �����
 
    while (i <= j) {
        while (list[i] < pivot)
            i++;
        while (list[j] > pivot)
            j--;
 
        if (i <= j) {
            tmp = list[i];
            list[i] = list[j];
            list[j] = tmp;
 
            i++;
            j--;
        }
    }
 
    //�Ӱ谪
    if (pivot > thres)
    {
        if (left < j)
            quickSort(list, thres, left, j);
        if (i < right)
            quickSort(list, thres, i, right);
    }
    else // �Ӱ谪���� ������ ���� ���� 
        insertionSort(list);
}
 
 void insertionSort(int list[]) {
  int i, j, key;
  // ���ؽ� 0�� �̹� ���ĵ� ������ �� �� �ִ�.
  for(i=1; i<MAX; i++){
    key = list[i]; // ���� ���Ե� ������ i��° ������ key ������ ����

    // ���� ���ĵ� �迭�� i-1�����̹Ƿ� i-1��°���� �������� �����Ѵ�.
    // j ���� ������ �ƴϾ�� �ǰ�
    // key ������ ���ĵ� �迭�� �ִ� ���� ũ�� j��°�� j+1��°�� �̵�
    for(j=i-1; j>=0 && list[j]>key; j--){
      list[j+1] = list[j]; // ���ڵ��� ���������� �̵�
    }

    list[j+1] = key;
  }
}

int main() {
    clock_t start = 0, finish = 0;
    float duration1, duration2;
    int threhold;
 
    //srand(time(NULL));
    srand(SEED);
    int arr1[MAX], arr2[MAX];
 
    cout << "������ ���� = 50000 \n�Ӱ谪�� �Է��ϼ���. " ;
    cin >> threhold; 
 
    for (int j = 0; j < MAX; j ++) { //������ ���� �迭 ����� 
        //arr1[j] = rand() % 9999 + 1;
        //arr2[j] = rand() % 9999 + 1; // �������� ���� 
        arr1[j] = MAX - j - 1;
		arr2[j] = MAX - j - 1; // �������� Ȥ�� ���������� ����  
    }
 
    cout << "merger sorting... "; // �������� ���� 
    start = clock();
    mergeSort(arr1, threhold, 0, MAX - 1 );
    finish = clock();
    duration1 = (float)(finish - start) / CLOCKS_PER_SEC;
  	cout << "done!!" << endl;

 
    cout << "quick sorting... "; // �� ���� ���� 
	start = clock();
    quickSort(arr2, threhold, 0, MAX - 1);
    finish = clock();
    duration2 = (float)(finish - start) / CLOCKS_PER_SEC;
 	cout << "done!!" << endl;
    
    cout.precision(7);
    cout <<"merger time = " <<  duration1 << "\nquick time = " << duration2 << endl; // ���� �ð� ��� 
  
 	return 0;
 }
