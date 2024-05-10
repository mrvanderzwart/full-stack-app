import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatchLogComponent } from './match-log/match-log.component';

import { AgGridAngular } from 'ag-grid-angular';
import { HttpClientModule } from '@angular/common/http';
import { NextMatchComponent } from './next-match/next-match.component';

@NgModule({
  declarations: [
    AppComponent,
    MatchLogComponent,
    NextMatchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AgGridAngular, 
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }