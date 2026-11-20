# `Item-keys-Items` branch information

`Item`s are now used exclusively as unique keys in `Items` collections.
Previously the digital existence of the `Item` object was monitored
(in essence) to prevent duplication. Now, each `Item` *must* exist in
an `Items` collection, where it is associated (read: it keys in a HashMap)
with an integer count. The count is monitered in all `Item` transfers to
prevent duplication. Count should always be 1 or greater (we remove 0 counts).

Note: Much of the code was written allowing count to be 0, but this shouldn't
break anything.

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