import { Component, OnInit } from '@angular/core';
import { ServiceProfilsService } from '../service-profils.service';

@Component({
  selector: 'jhi-page-profil',
  templateUrl: './page-profil.component.html',
  styleUrls: ['./page-profil.component.scss']
})
export class PageProfilComponent implements OnInit {
  tableau;

  constructor(private serviceProfil: ServiceProfilsService) {
    this.tableau = this.serviceProfil.profils;
  }
  ngOnInit() {}
}
