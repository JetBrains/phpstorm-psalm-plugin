<?php
function recheck_queue() {
    $batch_size = 100;
    $start = 0;
    $result_counts = Akismet_Admin::recheck_queue_portion($start, $batch_size);
    foreach ($result_counts as $key => $count) {
        $total_counts[<type value="int|string">$key</type>] = $count;
    }
}
