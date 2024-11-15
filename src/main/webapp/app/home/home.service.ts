import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "app/core/config/application-config.service";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class HomeService {
    protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

    constructor(protected http: HttpClient, 
        protected applicationConfigService: ApplicationConfigService
    ) {}

    downloadEntities(): Observable<void> {
        return this.http.get<void>(`${this.resourceUrl}/downloadEntities`);
    }


}