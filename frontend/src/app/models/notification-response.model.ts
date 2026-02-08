import { NotificationType } from "./notification-type.enum";
import { UserResponse } from "./user-response.model";

export interface NotificationResponse {
    id: string,
    actor: UserResponse,
    type: NotificationType,
    relatedEntityId: string,
    seen: boolean,
    createdAt: string
}