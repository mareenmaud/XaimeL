import { Component, OnInit } from '@angular/core';
import { ServiceStatsService } from 'app/service-stats.service';

@Component({
  providers: [ServiceStatsService],
  selector: 'jhi-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.scss']
})
export class StatsComponent implements OnInit {
  tab: any[];
  nombreTotalMessages: number;

  constructor(monService: ServiceStatsService) {
    this.tab = monService.mesExemples;
  }

  ngOnInit() {}
}
