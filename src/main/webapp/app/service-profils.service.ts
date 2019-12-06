import { Injectable } from '@angular/core';
import { profil } from './profil';

@Injectable({
  providedIn: 'root'
})
export class ServiceProfilsService {
  profils = [
    new profil('Latorre', 'Xavier', 'J ai traverse la rue et me voila, frontend'),
    new profil(
      'Delellis',
      'Mathieu',
      'Notre cher Team Leader, véritable moteur de l equipe et ' + 'gestionnaire de la bonne ambiance, frontend'
    ),
    new profil('Zemmouri', 'Samir', 'Samir, simplement Samir, backend'),
    new profil(
      'Madeira',
      'Cindy',
      'Avec son ame d artiste, elle nous propose son aide pour ' + 'que le site soit magnifique,webdesign-frontend'
    ),
    new profil(
      'Autricque',
      'Adrien',
      'Veritable expert fullstack, son aide est précieuse pour ' + 'que l application vous renvoie la bonne personne,backend'
    ),
    new profil(
      'Maud',
      'Mareen',
      'Toujours dans la joie et la bonne humeur, Mareen veille à' + ' la bonne mise en place du site, devops-backend'
    ),
    new profil('Dugauquier', 'Rémy', 'Notre cher ami belge venu en France pour' + ' gérer des données, base de données')
  ];

  constructor() {}
}
