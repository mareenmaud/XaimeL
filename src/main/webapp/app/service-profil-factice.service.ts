import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServiceProfilFacticeService {
  tab = [
    { nom: 'nom1', prenom: 'prenom1', messages: 'message1', photo: '/content/images/profilanonyme.png' },
    { nom: 'nom2', prenom: 'prenom2', messages: 'message2', photo: '/content/images/ProfilAnonyme2.png' },
    { nom: 'nom3', prenom: 'prenom3', messages: 'message3', photo: '/content/images/profilAnonyme3.png' },
    { nom: 'nom4', prenom: 'prenom4', messages: 'message4', photo: '/content/images/profilanonyme.png' },
    {
      nom: 'nom5',
      prenom: 'prenom5',
      messages: 'message5',
      photo: '/content/images/ProfilAnonyme2.png',
      sexe: 'homme',
      mdp: 'mdp1',
      mail: 'mail',
      preference: ['femme', 'gout1', 'gout2', 'age'],
      age: '18',
      pseudo: 'dd',
      notifs: true,
      match: ['message1', 'message2'],
      note: 3,
      interest: 'femme'
    },

    {
      nom: 'nom6',
      prenom: 'prenom6',
      messages: 'message6',
      photo: '/content/images/profilAnonyme3.jpg',
      sexe: 'femme',
      mdp: 'mdp2',
      mail: 'mail',
      preference: ['homme', 'gout5', 'gout2', 'age'],
      age: '19',
      pseudo: 'ff',
      notifs: false,
      match: ['message3', 'message4']
    }
  ];
  constructor() {}
}
