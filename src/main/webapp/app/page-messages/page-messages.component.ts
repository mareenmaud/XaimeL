import { Component, OnInit } from '@angular/core';
import { ServiceProfilFacticeService } from 'app/service-profil-factice.service';

@Component({
  selector: 'jhi-page-messages',
  templateUrl: './page-messages.component.html',
  providers: [ServiceProfilFacticeService],
  styleUrls: ['./page-messages.component.scss']
})
export class PageMessagesComponent implements OnInit {
  tab: any[];
  constructor(monservice: ServiceProfilFacticeService) {
    this.tab = monservice.tab;
  }

  ngOnInit() {}
}
