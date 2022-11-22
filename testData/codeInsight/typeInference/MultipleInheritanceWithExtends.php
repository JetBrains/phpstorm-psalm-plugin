<?php declare(strict_types=1);

interface AnimalInterface {}
interface DogInterface extends AnimalInterface {
    public function bark(): void;
}
interface HuskyInterface extends DogInterface {
    public function pullSledge(): void;
}

/** @template T of AnimalInterface */
interface AnimalRepositoryInterface {
    /** @return T */
    public function find(): AnimalInterface;
}

/**
 * @template T of DogInterface
 * @extends AnimalRepositoryInterface<T>
 */
interface DogRepositoryInterface extends AnimalRepositoryInterface {}

/** @extends DogRepositoryInterface<HuskyInterface> */
interface HuskyRepositoryInterface extends DogRepositoryInterface {}
interface AA extends HuskyRepositoryInterface {}
class HuskyProvider {
    public function __construct(private AA $huskyRepository) {}

    public function get(): HuskyInterface
    {
        <type value="HuskyInterface|AnimalInterface">$husky = $this->huskyRepository->find()</type>;
    }
}
