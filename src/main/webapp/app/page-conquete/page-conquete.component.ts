import { Component, OnInit } from '@angular/core';
import { ServiceProfilFacticeService } from 'app/service-profil-factice.service';

@Component({
  selector: 'jhi-page-conquete',
  templateUrl: './page-conquete.component.html',
  providers: [ServiceProfilFacticeService],
  styleUrls: ['./page-conquete.component.scss']
})
export class PageConqueteComponent implements OnInit {
  tab: any[];
  constructor(monservice: ServiceProfilFacticeService) {
    this.tab = monservice.tab;
  }

  ngOnInit() {}
}
