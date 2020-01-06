import { Component, OnInit } from '@angular/core';
import { ServiceProfilFacticeService } from 'app/service-profil-factice.service';

@Component({
  selector: 'jhi-page-profil-client',
  templateUrl: './page-profil-client.component.html',
  providers: [ServiceProfilFacticeService],
  styleUrls: ['./page-profil-client.component.scss']
})
export class PageProfilClientComponent implements OnInit {
  tab: any[];
  constructor(serviceF: ServiceProfilFacticeService) {
    this.tab = serviceF.tab;
  }

  ngOnInit() {}
}
