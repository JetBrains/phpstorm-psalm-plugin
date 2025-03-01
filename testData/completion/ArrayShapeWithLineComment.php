<?php

/**
 * @return array{
 *     // comment
 *     key1: int, // Comment
	 *     // Comment
 *     key2: array { // Comment
 *         key2_1: string, // Comment
 *     }
 * }
 */
function test(): array {
	return [
		'key1' => 1,
		'key2' => [
			'key2_1' => 'test',
		],
	];
}

test()['<caret>'];