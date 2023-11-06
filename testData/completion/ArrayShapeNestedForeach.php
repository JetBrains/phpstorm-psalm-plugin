<?php

/**
 * @param array{
 *     int,
 *     array {
 *              objects: array{
 *                  type: string,
 *                  title: string
 *              },
 *              notifications: array{array{
 *                  url: string,
 *                 title: array {
 *                    key : array{array{
 *     somekey: string
 *              }}}
 * }} }}$notificationGroup
 */
function foo(array $notificationGroup)
{
    foreach ($notificationGroup as $notificationsAndObjects) {
        foreach ($notificationsAndObjects['notifications'] as $notification) {
            foreach ($notification['title']['key'] as $n) {
                $n['<caret>'];
            }
        }
    }
}
