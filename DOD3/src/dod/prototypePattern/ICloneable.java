package dod.prototypePattern;

import dod.Communicator.GameCommunicator;

public interface ICloneable<TEntity> {
    TEntity Clone(GameCommunicator comm);
}
