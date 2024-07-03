package dev.crmodders.puzzle.access_manipulators.transformers;

import org.objectweb.asm.Opcodes;

public enum ClassModifier {

    // Regular Modifiers
    PUBLIC(Opcodes.ACC_PUBLIC, new int[]{ Opcodes.ACC_PRIVATE, Opcodes.ACC_PROTECTED }),
    PRIVATE(Opcodes.ACC_PRIVATE, new int[]{ Opcodes.ACC_PUBLIC, Opcodes.ACC_PROTECTED }),
    PROTECTED(Opcodes.ACC_PROTECTED, new int[]{ Opcodes.ACC_PUBLIC, Opcodes.ACC_PRIVATE }),

    // Mutable Modifiers
    MUTABLE(Opcodes.ACC_PUBLIC, new int[]{ Opcodes.ACC_FINAL }, true, new int[]{ Opcodes.ACC_PUBLIC }),
    PUBLIC_MUTABLE(Opcodes.ACC_PUBLIC, new int[]{ Opcodes.ACC_FINAL, Opcodes.ACC_PRIVATE, Opcodes.ACC_PROTECTED }),
    PRIVATE_MUTABLE(Opcodes.ACC_PRIVATE, new int[]{ Opcodes.ACC_FINAL, Opcodes.ACC_PUBLIC, Opcodes.ACC_PROTECTED }),
    PROTECTED_MUTABLE(Opcodes.ACC_PROTECTED, new int[]{ Opcodes.ACC_FINAL, Opcodes.ACC_PUBLIC, Opcodes.ACC_PRIVATE }),

    // Immutable Modifiers
    PUBLIC_IMMUTABLE(new int[] { Opcodes.ACC_PUBLIC, Opcodes.ACC_FINAL }, new int[]{ Opcodes.ACC_PRIVATE, Opcodes.ACC_PROTECTED }),
    PRIVATE_IMMUTABLE(new int[] { Opcodes.ACC_PRIVATE, Opcodes.ACC_FINAL }, new int[]{ Opcodes.ACC_PUBLIC, Opcodes.ACC_PROTECTED }),
    PROTECTED_IMMUTABLE(new int[] { Opcodes.ACC_PROTECTED, Opcodes.ACC_FINAL }, new int[]{ Opcodes.ACC_PUBLIC, Opcodes.ACC_PRIVATE });

    final int[] newFlags;
    final int[] incompatibleFlags;
    boolean interfaceCheck;
    final int[] backupFlags;

    ClassModifier(int[] newFlags, int[] incompatibleFlags) {
        this.newFlags = newFlags;
        this.incompatibleFlags = incompatibleFlags;
        this.backupFlags = new int[0];
    }

    ClassModifier(int newFlag, int[] incompatibleFlags) {
        this.newFlags = new int[]{newFlag};
        this.incompatibleFlags = incompatibleFlags;
        this.backupFlags = new int[0];
    }

    ClassModifier(int[] newFlags, int[] incompatibleFlags, boolean interfaceCheck, int[] backupFlags) {
        this.newFlags = newFlags;
        this.incompatibleFlags = incompatibleFlags;
        this.interfaceCheck = interfaceCheck;
        this.backupFlags = backupFlags;
    }

    ClassModifier(int newFlag, int[] incompatibleFlags, boolean interfaceCheck, int[] backupFlags) {
        this.newFlags = new int[]{newFlag};
        this.incompatibleFlags = incompatibleFlags;
        this.interfaceCheck = interfaceCheck;
        this.backupFlags = backupFlags;
    }

    int updateFlags(int oldFlags) {
        int currentFlags = oldFlags;

        for (int flag : incompatibleFlags) {
            if ((oldFlags & flag) != 0) {
                currentFlags = (currentFlags ^ flag);
            }
        }

        for (int flag : newFlags) {
            currentFlags |= flag;
        }

        if (interfaceCheck) {
            if ((oldFlags & Opcodes.ACC_INTERFACE) != 0 && (oldFlags & Opcodes.ACC_STATIC) != 0) {
                for (int flag : backupFlags) {
                    currentFlags |= flag;
                }
            }
        }

        return currentFlags;
    }

}
