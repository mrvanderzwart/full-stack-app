import { Component } from '@angular/core';
import { ColDef } from 'ag-grid-community';
import { HttpClient } from '@angular/common/http';

import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-quartz.css';

interface Match {
  id: number;
  matchdate: string;
  opponent: string;
  result: string;
  competition: string;
}

@Component({
  selector: 'app-match-log',
  templateUrl: './match-log.component.html',
  styleUrl: './match-log.component.css'
})
export class MatchLogComponent {
  themeClass = "ag-theme-quartz";
  rowData: Match[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<Match[]>('http://localhost:8080/getMatches')
      .subscribe(
        (data) => {
          this.rowData = data;
        },
        (error) => {
          console.error('Error fetching data:', error);
        }
      );
  }

  reload() {
    this.http.delete('http://localhost:8080/clearMatches')
      .subscribe(
        (data) => { },
        (error) => {
          console.error('Error fetching data:', error);
        }
      );
    this.http.post('http://localhost:8080/parseMatches', '')
      .subscribe(
        (data) => { },
        (error) => {
          console.error('Error fetching data:', error);
        }
      );
  }

  columnDefs: ColDef[] = [
    { headerName: 'Match Date', field: 'matchdate' },
    { headerName: 'Opponent', field: 'opponent' },
    { headerName: 'Result', field: 'result', cellStyle: params => {
          if (params.value === 'W')
            return {color: 'green'}
          else if (params.value == 'L')
            return {color: 'red'}
          else  
            return {color: 'orange'}
    }},
    { headerName: 'Competition', field: 'competition' }
  ];
}