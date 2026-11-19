## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

# Zuul-Isaac

This is an assignment for CSCI-125.

## Hard-coded rules

### Confirmed rules
 - If two `Item`s in an `Items` collection have the same name,
    they must match in every way (description, weight, (attributes??)...)
 - `Room`s and `Player`s have `Items` collection of `Item`s

### Untested rules

 - `World` has `Room`s, all with unique names
 - `World` has `Player`s, all with unique names
   - Trying to add a `Player` with the same name renames "Name" to "Name(2)".
      this keeps going.


### Unimplemented rules

 - `Game` has only one active `World`
 - Every `Player` belongs to a `Room` (known by both)
 - Every `Room` belongs to `World` (known by both)
 - Every `Player` in a `Room` is known by `World`


## Soft-coded rules

These are general ways the project is used, but
not strictly enforced

 - Every `Item` belongs to an `Items` collection
