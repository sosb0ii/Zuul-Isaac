# Zuul-Isaac

This is an assignment for CSCI-125.

## Hard-coded rules

### Confirmed rules

### Untested rules

 - If two `Item`s in an `Items` collection have the same name,
    they must match in every way (description, weight, (attributes??)...)
 - `Room`s and `Player`s have `Items` collection of `Item`s
 - `World` has `Room`s, all with unique names
 - `World` has `Player`s, all with unique names
   - Trying to add a `Player` with the same name renames "Name" to "Name(2)".
      this keeps going.
 - Every `Item` belongs to an `Items` collection
 - Every `Player` belongs to a `Room` (known by both)
 - Every `Room` belongs to `World` (known by both)
 - Every `Player` in a `Room` is known by `World` (only known by World)


### Unimplemented rules

 - `Game` has only one active `World`


## Soft-coded rules

These are general ways the project is used, but
not strictly enforced

# Further ideas

 - I'm wondering if Player and Room should be Children of the same class.
   Half the code is the same! This would be a seperate branch
 - Use a set that tracks multiplicity in `Items`.
 This would make the details of memory location irrelevant.
 Instead `Item.equals()` would be redefined, to alter the underlying behavior of
 logic done on the items.
 This object equivalence would be determined by observing all relevant fields.
 One instance of the object in that equivalence class would be the key to its multiplicity
 in `HashMap<Item> contents`.
 The add(newItem) method would check containsKey(newItem), which would be true if the existing key
 was equivalent in the relevant fields.
 If true, it would increment the multiplicity count.
 If false, it would check that the new item has a unique name (else fail)
 and put a new item with multiplicity 1 to contents.
 The remove(itemName) method would check every key,
 to see if it had that name (share method with that logic in add).
 When that key is found, check the multiplicity.
 If multiplicity >0, (else fail) subtract 1 from multiplicity and return the Item.
 Main idea: `Item`s are only used as keys for the contents of `Items` collections.