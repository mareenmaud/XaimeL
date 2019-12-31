import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServiceProfilFacticeService {
  tab = [
    { nom: 'nom1', prenom: 'prenom1', messages: 'message1', photo: '/content/images/steph.jpg' },
    { nom: 'nom2', prenom: 'prenom2', messages: 'message2', photo: '/content/images/mac.jpg' },
    { nom: 'nom3', prenom: 'prenom3', messages: 'message3', photo: '/content/images/mac.jpg' },
    { nom: 'nom4', prenom: 'prenom4', messages: 'message4', photo: '/content/images/steph.jpg' }
  ];
  constructor() {}
}
