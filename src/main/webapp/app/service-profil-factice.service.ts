import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServiceProfilFacticeService {
  tab = [
    { nom: 'nom1', prenom: 'prenom1', messages: 'message1' },
    { nom: 'nom2', prenom: 'prenom2', messages: 'message2' },
    { nom: 'nom3', prenom: 'prenom3', messages: 'message3' },
    { nom: 'nom4', prenom: 'prenom4', messages: 'message4' }
  ];
  constructor() {}
}
