import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServiceProfilFacticeService {
  tab = [
    { nom: 'nom1', prenom: 'prenom1', messages: 'message1', photo: '/content/images/steph.jpg' },
    { nom: 'nom2', prenom: 'prenom2', messages: 'message2', photo: '/content/images/mac.jpg' },
    { nom: 'nom3', prenom: 'prenom3', messages: 'message3', photo: '/content/images/mac.jpg' },
    { nom: 'nom4', prenom: 'prenom4', messages: 'message4', photo: '/content/images/steph.jpg' },
    {
      nom: 'nom5',
      prenom: 'prenom5',
      messages: 'message5',
      photo: '/content/images/steph.jpg',
      sexe: 'homme',
      mdp: 'mdp1',
      mail: 'mail',
      preference: ['femme', 'gout1', 'gout2', 'age'],
      age: '18',
      pseudo: 'dd',
      notifs: true,
      match: ['message1', 'message2']
    },

    {
      nom: 'nom6',
      prenom: 'prenom6',
      messages: 'message6',
      photo: '/content/images/steph.jpg',
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
