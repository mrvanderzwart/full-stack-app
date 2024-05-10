import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

interface Match {
  id: number;
  matchdate: string;
  opponent: string;
  result: string;
  competition: string;
}

@Component({
  selector: 'app-next-match',
  templateUrl: './next-match.component.html',
  styleUrl: './next-match.component.css'
})
export class NextMatchComponent {
  opponent: String = "";
  date: String = "";
  clickMessage: String = "";
  result: String = "";

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<Match>('http://localhost:8080/getNextMatch')
      .subscribe(
        (data) => {
          this.opponent = data.opponent;
          this.date = data.matchdate;
        },
        (error) => {
          console.error('Error fetching data:', error);
        }
      );
  }

  predict() {
    if (this.clickMessage != "")
      return;

    this.clickMessage = "predicting ...";

    this.http.get('http://localhost:8080/getPrediction', { responseType: 'text'})
      .subscribe(
        (data: string) => {
          this.clickMessage = "Ajax predicted to " + data + " against " + this.opponent;
          this.result = data;
        },
        (error) => {
          console.error('Error fetching data:', error);
        }
      );
  }

  getColor() {
    if (this.result === 'win') {
      return 'green';
    } else if (this.result === 'lose') {
      return 'red';
    } else if (this.result === 'draw') {
      return 'orange';
    }

    return 'default';
  }
}