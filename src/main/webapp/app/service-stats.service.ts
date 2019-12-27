import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServiceStatsService {
  mesExemples = [
    {
      nombreConquetes: 3,
      nombreInvitationsR: 5,
      nombreInvitationsE: 2,
      nombreMessagesE: 3,
      nombreMessagesR: 2,
      nombreDiscussionsE: 1,
      nombreDiscussionsA: 3
    },

    {
      nombreConquetes: 4,
      nombreInvitationsR: 2,
      nombreInvitationsE: 5,
      nombreMessagesE: 13,
      nombreMessagesR: 2,
      nombreDiscussionsE: 3,
      nombreDiscussionsA: 7
    },

    {
      nombreConquetes: 4,
      nombreInvitationsR: 2,
      nombreInvitationsE: 3,
      nombreMessagesE: 7,
      nombreMessagesR: 7,
      nombreDiscussionsE: 8,
      nombreDiscussionsA: 3
    }
  ];
  constructor() {}
}
