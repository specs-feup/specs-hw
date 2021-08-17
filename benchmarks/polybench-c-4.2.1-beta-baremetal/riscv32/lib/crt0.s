.section .init, "ax"
.global _start
_start:
    .cfi_startproc
    .cfi_undefined ra
    .option push
    .option norelax
    
    li t0, 0x6000 # a macro for MSTATUS_FS can be defined with this value if desired
    csrs mstatus, t0 # sets the status bits of the FPU to "enabled", and 
    fscsr x0 # sets the FPU operating and rounding mode    
    
    la gp, __global_pointer$
    .option pop
    la sp, __stack_top
    add s0, sp, zero
    call main
    tail exit
    .cfi_endproc
    .end

