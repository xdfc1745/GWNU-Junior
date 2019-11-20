#include <iostream>
#include <time.h>
#include <stdlib.h>

using namespace std;

#define MAX 50000 // 배열의 크기
#define SEED 3
int sorted[MAX]; // 병합을 위한 임시배열
void insertionSort(int list[]);
 
void merge(int list[], int left, int mid, int right)
{
    int i, j, k, l;
    i = left; // i는 정렬된 왼쪽리스트에 대한 인덱스
    j = mid + 1; // j는 정렬된 오른쪽리스트에 대한 인덱스
    k = left;  // k는 정렬될 리스트에 대한 인덱스
    
    while (i <= mid && j <= right) {//분할한 배열 합병 
        if (list[i] <= list[j])
            sorted[k++] = list[i ++];
        else
            sorted[k++] = list[j ++];
    }
    if (i > mid) { // 남아 있는 모든 숫자 배열에 복사
        for (l = j; l <= right; l ++)
            sorted[k++] = list[l];
    }
    else { 
        for (l = i; l <= mid; l ++)
            sorted[k++] = list[l];
    }
    
    for (l = left; l <= right; l ++) // 임시 배열에 정렬된 숫자들 원래 배열로 복사 
        list[l] = sorted[l];
}

void mergeSort(int list[], int thres, int left, int right) {
    int mid;
 
    if (left < right) {
        mid = (left + right) / 2; 
 
        if (mid > thres) {
            mergeSort(list, thres, left, mid);  // 부분리스트 정렬
            mergeSort(list, thres, mid + 1, right); //부분리스트 정렬
 
            merge(list, left, mid, right);   // 합병
        }
        else
            insertionSort(list);
    }
}

void quickSort(int list[], int thres, int left, int right) //left = 맨 앞 , right = 맨 뒤
{
    int i = left, j = right;
    int tmp;
    int pivot = list[(left + right) / 2]; // 가운데 값을 pivot으로 사용함
 
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
 
    //임계값
    if (pivot > thres)
    {
        if (left < j)
            quickSort(list, thres, left, j);
        if (i < right)
            quickSort(list, thres, i, right);
    }
    else // 임계값보다 작으면 삽입 정렬 
        insertionSort(list);
}
 
 void insertionSort(int list[]) {
  int i, j, key;
  // 인텍스 0은 이미 정렬된 것으로 볼 수 있다.
  for(i=1; i<MAX; i++){
    key = list[i]; // 현재 삽입될 숫자인 i번째 정수를 key 변수로 복사

    // 현재 정렬된 배열은 i-1까지이므로 i-1번째부터 역순으로 조사한다.
    // j 값은 음수가 아니어야 되고
    // key 값보다 정렬된 배열에 있는 값이 크면 j번째를 j+1번째로 이동
    for(j=i-1; j>=0 && list[j]>key; j--){
      list[j+1] = list[j]; // 레코드의 오른쪽으로 이동
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
 
    cout << "원소의 개수 = 50000 \n임계값을 입력하세요. " ;
    cin >> threhold; 
 
    for (int j = 0; j < MAX; j ++) { //정렬한 숫자 배열 만들기 
        //arr1[j] = rand() % 9999 + 1;
        //arr2[j] = rand() % 9999 + 1; // 랜덤숫자 생성 
        arr1[j] = MAX - j - 1;
		arr2[j] = MAX - j - 1; // 내림차순 혹은 오름차순의 숫자  
    }
 
    cout << "merger sorting... "; // 병합정렬 실행 
    start = clock();
    mergeSort(arr1, threhold, 0, MAX - 1 );
    finish = clock();
    duration1 = (float)(finish - start) / CLOCKS_PER_SEC;
  	cout << "done!!" << endl;

 
    cout << "quick sorting... "; // 퀵 정렬 실행 
	start = clock();
    quickSort(arr2, threhold, 0, MAX - 1);
    finish = clock();
    duration2 = (float)(finish - start) / CLOCKS_PER_SEC;
 	cout << "done!!" << endl;
    
    cout.precision(7);
    cout <<"merger time = " <<  duration1 << "\nquick time = " << duration2 << endl; // 정렬 시간 출력 
  
 	return 0;
 }
