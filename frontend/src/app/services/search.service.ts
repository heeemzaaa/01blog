import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SearchService {

  http = inject(HttpClient);

  search(keyword: string, page: number = 0) {
  return this.http.get<any>(
    `http://localhost:8080/api/search?keyword=${keyword}&page=${page}&size=5`
  );
}

}
